package com.example.Loc.Controller;

import com.example.Loc.Modal.*;
import com.example.Loc.Service.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;


@RestController
@RequestMapping(path = "/api/bill")
public class BillAPIController {
    @Autowired
    private IBillService billService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IBillDetailService billDetailService;
    @Autowired
    private ICartService cartService;
    @Autowired
    private ICartItemService cartItemService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private IProductService productService;

    @GetMapping
    public ResponseEntity<?> getAllBill() {
        return new ResponseEntity<Response>(new Response(true, "Thành công",
                billService.findAll()), HttpStatus.OK);
    }
    @PostMapping(path = "getAllBillByIdAccount")
    public ResponseEntity<?> getAllBillByIdAccount(@Validated @RequestParam("idAccount") Long idAccount) {
        List<Bill> list = billService.findByAccountIdOrderByCreatedAtDesc(idAccount);
        return new ResponseEntity<Response>(new Response(true, "Thành công",
                list), HttpStatus.OK);
    }
    @PostMapping(path = "getAllPageBill")
    public ResponseEntity<?> getAllBill(
            @Validated @RequestParam("page") Long page,
            @Validated @RequestParam("size") Long size
    ) {
        Pageable pageable = PageRequest.of(page.intValue(), size.intValue(), Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Bill> page1 = billService.findAll(pageable);
        return new ResponseEntity<Response>(new Response(true, "Thành công",
                page1), HttpStatus.OK);
    }
    @PostMapping(path = "getPageBillDate")
    public ResponseEntity<?> getPageBillDate(
            @Validated @RequestParam("dateStart") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
            @Validated @RequestParam("dateEnd") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd,
            @Validated @RequestParam("page") Long page,
            @Validated @RequestParam("size") Long size
    ) {
        Page<Bill> page1 = billService.findPageDate(dateStart,dateEnd,page.intValue(),size.intValue());
        return new ResponseEntity<Response>(new Response(true, "Thành công",
                page1), HttpStatus.OK);
    }
    @Transactional
    @PostMapping(path = "order")
    public ResponseEntity<?> order(
            @Validated @RequestParam("idAccount") Long idAccount,
            @Validated @RequestParam("address") String address
    ) {
        Optional<Cart> cartOpt = cartService.findCartInIdAccount(idAccount);
        if (!cartOpt.isPresent()) {
            return new ResponseEntity<>(new Response(false, "Cart not found", null), HttpStatus.OK);
        }

        Cart cart = cartOpt.get();

        if (address.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Request error", null), HttpStatus.OK);
        }

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Your cart is empty", null), HttpStatus.OK);
        }

        // Kiểm tra sản phẩm trong giỏ hàng
        List<CartItem> cartItems = new ArrayList<>(cart.getCartItems());
        for (CartItem item : cartItems) {
            if (item.getProduct().getStockCount() == 0) {
                return new ResponseEntity<>(new Response(false, "Product id " + item.getProduct().getId() + "  is out of stock", null), HttpStatus.OK);
            }
        }

        // Tạo Bill
        Optional<Account> acc = accountService.findById(idAccount);
        if (!acc.isPresent()) {
            return new ResponseEntity<>(new Response(false, "Account not found", null), HttpStatus.OK);
        }
        List<BillDetail> lstBillDt = new ArrayList<>();
        Long total = 0L;
        Bill bill = new Bill();
        bill.setAccount(acc.get());
        bill.setStatus(0L); // Đơn hàng mới
        billService.save(bill);
        for (CartItem item : cartItems) {
            // Tạo chi tiết hóa đơn
            BillDetail billDetail = new BillDetail();
            billDetail.setProduct(item.getProduct());
            billDetail.setQuantity(item.getQuantity());
            billDetail.setBill(bill);
            lstBillDt.add(billDetail);

            // Tính tổng
            total += item.getProduct().getPrice() * item.getQuantity();

            // Cập nhật sản phẩm
            Product product = item.getProduct();
            product.setStockCount(product.getStockCount() - item.getQuantity());
            product.setSoldCount(product.getSoldCount() + item.getQuantity());
            productService.save(product);

            billDetailService.save(billDetail);
        }
        for (CartItem item : cartItems){
            cartItemService.delete(item);
        }
        cart.getCartItems().clear();
        cartService.save(cart);

        // Cập nhật đơn hàng
        total += 70000; // Phí ship
        bill.setTotal(total);
        bill.setBillDetails(lstBillDt);
        bill.setAddress(address);
        billService.save(bill);

        return new ResponseEntity<>(new Response(true, "Thành công", bill), HttpStatus.OK);
    }

    @PostMapping(path = "changeStatusBill")
    public ResponseEntity<?> changeStatusBill(
            @Validated @RequestParam("idBill") Long idBill
    ) {
        Optional<Bill> bill = billService.findById(idBill);
        if (bill.isEmpty()){
            return new ResponseEntity<Response>(new Response(false, "Invoice not found",
                    null), HttpStatus.OK);
        }
        if (bill.get().getStatus()<2){
            bill.get().setStatus(bill.get().getStatus()+1);
            billService.save(bill.get());
        }
        return new ResponseEntity<Response>(new Response(true, "Thành công",
                bill.get()), HttpStatus.OK);
    }
    @PostMapping(path = "cancelBill")
    public ResponseEntity<?> cancelBill(
            @Validated @RequestParam("idBill") Long idBill
    ) {
        Optional<Bill> bill = billService.findById(idBill);
        if (bill.isEmpty()){
            return new ResponseEntity<Response>(new Response(false, "Invoice not found",
                    null), HttpStatus.OK);
        }
        if (bill.get().getStatus() == 3){
            return new ResponseEntity<Response>(new Response(false, "Invoice cancel",
                    null), HttpStatus.OK);
        }
        List<BillDetail> lstBillDT = bill.get().getBillDetails();
        for (BillDetail billDetail : lstBillDT){
            Product product = billDetail.getProduct();
            Long soldCount = product.getSoldCount();
            Long stockCount = product.getStockCount();
            product.setSoldCount(soldCount-billDetail.getQuantity());
            product.setStockCount(stockCount+billDetail.getQuantity());
            productService.save(product);
        }
        bill.get().setStatus(3L);
        billService.save(bill.get());
        return new ResponseEntity<Response>(new Response(true, "Thành công",
                bill.get()), HttpStatus.OK);
    }

    @PostMapping(path = "getBilllByBillID")
    public ResponseEntity<?> getBillDetailByBillID(
            @Validated @RequestParam("idBill") Long idBill
    ) {
        Optional<Bill> bill = billService.findById(idBill);
        if (bill.isEmpty()){
            return new ResponseEntity<Response>(new Response(false, "Invoice not found",
                    null), HttpStatus.OK);
        }
        return new ResponseEntity<Response>(new Response(true, "Thành công",
                bill.get()), HttpStatus.OK);
    }
    @PostMapping(path = "findTopSellingProductsByMonth")
    public ResponseEntity<?> findTopSellingProductsByMonth(
            @Validated @RequestParam("month") Long month
    ) {
        Pageable topTen = PageRequest.of(0, 10);
        List<Object[]> result  = billService.findTopSellingProductsByMonth(LocalDate.now().getYear(),month.intValue(),topTen);
        return new ResponseEntity<Response>(new Response(true, "Thành công",
                result), HttpStatus.OK);
    }
    @PostMapping(path = "getMonthlyRevenueByYear")
    public ResponseEntity<?> getMonthlyRevenueByYear(
            @Validated @RequestParam("year") Long year
    ) {
        List<Object[]> objects = billService.getMonthlyRevenueByYear(year.intValue());
        Map<Integer, Long> revenueMap = new HashMap<>();
        for (Object[] obj : objects) {
            Integer month = (Integer) obj[0];
            Long total = (Long) obj[1];
            revenueMap.put(month, total);
        }

        List<Object[]> result = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            result.add(new Object[]{i, revenueMap.getOrDefault(i, 0L)});
        }
        return new ResponseEntity<Response>(new Response(true, "Thành công",
                objects), HttpStatus.OK);
    }

}
