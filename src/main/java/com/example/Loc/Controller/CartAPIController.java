package com.example.Loc.Controller;

import com.example.Loc.Modal.*;
import com.example.Loc.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/cart")
public class CartAPIController {
    @Autowired
    private ICartService icartService;
    @Autowired
    private ICartItemService iCartItemService;
    @Autowired
    private IAccountService iAccountService;
    @Autowired
    private IProductService iProductService;

    @GetMapping
    public ResponseEntity<?> getAllBill() {
        return new ResponseEntity<Response>(new Response(true, "Thành công",
                icartService.findAll()), HttpStatus.OK);

        }
    @PostMapping(path = "/getCountItemCart")
    public ResponseEntity<?> getCategory(@Validated @RequestParam("idAccount") Long id) {
        long count = icartService.countItemInCart(id);
        return new ResponseEntity<Response>(new Response(true, "Thành công",
                    count), HttpStatus.OK);
    }
    @PostMapping(path = "/getItemCartByIDAccount")
    public ResponseEntity<?> getItemCartByIDAccount(@Validated @RequestParam("idAccount") Long id) {
        Optional<Cart> cart = icartService.findCartInIdAccount(id);
        if (cart.isPresent()) {
            return new ResponseEntity<Response>(new Response(true, "Thành công",
                    cart.get()), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<Response>(new Response(false, "Ko tim thay",
                    null), HttpStatus.OK);
        }
    }
    @PostMapping(path = "/addItemInCart")
    public ResponseEntity<?> addItemInCart(@Validated @RequestParam("idAccount") Long id,
                                           @Validated @RequestParam("idProduct") Long idProduct) {
        Optional<Cart> cart = icartService.findCartInIdAccount(id);
        Optional<Product> product = iProductService.findById(idProduct);
        if (cart.isPresent()) {
            List<CartItem> lstCartItem = cart.get().getCartItems();
            if (product.isPresent()) {
                for (CartItem item : lstCartItem) {
                    if (item.getProduct().getId() == product.get().getId()) {
                        if (product.get().getStockCount() > 0) {
                            item.setQuantity(item.getQuantity() + 1);
                            iCartItemService.save(item);
                            return new ResponseEntity<Response>(new Response(true, "Thêm Thành công",
                                    cart.get()), HttpStatus.OK);
                        }
                    }
                }
                if (product.get().getStockCount() > 0) {
                    CartItem cartItem = new CartItem();
                    cartItem.setCart(cart.get());
                    cartItem.setQuantity(1L);
                    cartItem.setProduct(product.get());
                    iCartItemService.save(cartItem);
                    lstCartItem.add(cartItem);
                    cart.get().setCartItems(lstCartItem);
                    icartService.save(cart.get());
                    return new ResponseEntity<Response>(new Response(true, "Thêm Thành công",
                            cart.get()), HttpStatus.OK);
                } else {
                    return new ResponseEntity<Response>(new Response(false, "Đã hết hàng",
                            cart.get()), HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<Response>(new Response(false, "Không tìm thấy sản phẩm",
                        cart.get()), HttpStatus.OK);
            }
        }
        else{
            return new ResponseEntity<Response>(new Response(false, "Không tìm thấy Cart",
                    cart.get()), HttpStatus.OK);
        }
    }
    @PostMapping(path = "/removeItemInCart")
    public ResponseEntity<?> removeItemInCart(@Validated @RequestParam("idAccount") Long id,
                                              @Validated @RequestParam("idProduct") Long idProduct) {
        Optional<Cart> cartOpt = icartService.findCartInIdAccount(id);
        Optional<Product> productOpt = iProductService.findById(idProduct);

        if (cartOpt.isPresent() && productOpt.isPresent()) {
            Cart cart = cartOpt.get();
            Product product = productOpt.get();
            List<CartItem> lstCartItem = cart.getCartItems();

            CartItem toRemove = null;
            for (CartItem item : lstCartItem) {
                if (item.getProduct().getId().equals(product.getId())) {
                    item.setQuantity(item.getQuantity() - 1);
                    if (item.getQuantity() <= 0) {
                        toRemove = item;
                    } else {
                        iCartItemService.save(item);
                    }
                    break;
                }
            }

            if (toRemove != null) {
                lstCartItem.remove(toRemove);
                iCartItemService.delete(toRemove);
            }

            cart.setCartItems(lstCartItem); // Cập nhật lại danh sách
            icartService.save(cart);

            return new ResponseEntity<>(new Response(true, "Xóa thành công", cart), HttpStatus.OK);
        }

        return new ResponseEntity<>(new Response(false, "Không tìm thấy giỏ hàng hoặc sản phẩm", null), HttpStatus.OK);
    }
    @PostMapping(path = "/deleteItemInCart")
    public ResponseEntity<?> deleteItemInCart(@Validated @RequestParam("idAccount") Long id,
                                              @Validated @RequestParam("idProduct") Long idProduct) {
        Optional<Cart> cartOpt = icartService.findCartInIdAccount(id);
        Optional<Product> productOpt = iProductService.findById(idProduct);

        if (cartOpt.isPresent() && productOpt.isPresent()) {
            Cart cart = cartOpt.get();
            Product product = productOpt.get();
            List<CartItem> lstCartItem = cart.getCartItems();

            CartItem toRemove = null;
            for (CartItem item : lstCartItem) {
                if (item.getProduct().getId().equals(product.getId())) {
                    toRemove = item;
                    break;
                }
            }

            if (toRemove != null) {
                lstCartItem.remove(toRemove);
                iCartItemService.delete(toRemove);
            }
            else{
                return new ResponseEntity<>(new Response(false, "Không tìm thấy giỏ hàng hoặc sản phẩm", null), HttpStatus.OK);
            }

            cart.setCartItems(lstCartItem);
            icartService.save(cart);

            return new ResponseEntity<>(new Response(true, "Xóa thành công", cart), HttpStatus.OK);
        }

        return new ResponseEntity<>(new Response(false, "Không tìm thấy giỏ hàng hoặc sản phẩm", null), HttpStatus.OK);
    }
}