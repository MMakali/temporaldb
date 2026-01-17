package com.temporaldb.demo.controller;

import com.temporaldb.demo.model.Product;
import com.temporaldb.demo.model.ProductForm;
import com.temporaldb.demo.model.ProductHistory;
import com.temporaldb.demo.model.InventoryStats;
import com.temporaldb.demo.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Web controller for product inventory management.
 */
@Slf4j
@Controller
@RequestMapping("/")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Dashboard - main page.
     */
    @GetMapping
    public String dashboard(Model model) {
        List<Product> products = productService.getAllProducts();
        InventoryStats stats = productService.getStats();

        model.addAttribute("products", products);
        model.addAttribute("stats", stats);

        return "dashboard";
    }

    /**
     * Products list page.
     */
    @GetMapping("products")
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products";
    }

    /**
     * Show product detail page.
     */
    @GetMapping("products/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProduct(id);
        if (product == null) {
            return "redirect:/products?error=notfound";
        }

        List<ProductHistory> productHistory = productService.getProductHistory(id);
        model.addAttribute("product", product);
        model.addAttribute("history", productHistory);

        return "product-detail";
    }

    /**
     * Show new product form.
     */
    @GetMapping("products/new")
    public String newProductForm(Model model) {
        model.addAttribute("form", new ProductForm());
        return "product-form";
    }

    /**
     * Handle new product submission.
     */
    @PostMapping("products")
    public String createProduct(@ModelAttribute ProductForm form, RedirectAttributes redirect) {
        try {
            Product created = productService.addProduct(form);
            redirect.addFlashAttribute("success", "Product created: " + created.getName());
            return "redirect:/products/" + created.getId();
        } catch (Exception e) {
            log.error("Error creating product", e);
            redirect.addFlashAttribute("error", "Failed to create product");
            return "redirect:/products/new";
        }
    }

    /**
     * Show edit product form.
     */
    @GetMapping("products/{id}/edit")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProduct(id);
        if (product == null) {
            return "redirect:/products?error=notfound";
        }

        ProductForm form = new ProductForm(
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getQuantity(),
                product.getDescription()
        );

        // Add these two lines:
        List<ProductHistory> history = productService.getProductHistory(id);
        model.addAttribute("product", product);
        model.addAttribute("history", history);

        model.addAttribute("form", form);
        model.addAttribute("id", id);
        return "product-edit";
    }

    /**
     * Handle product update.
     */
    @PostMapping("products/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute ProductForm form, RedirectAttributes redirect) {
        try {
            productService.updateProduct(id, form);
            redirect.addFlashAttribute("success", "Product updated");
            return "redirect:/products/" + id;
        } catch (Exception e) {
            log.error("Error updating product", e);
            redirect.addFlashAttribute("error", "Failed to update product");
            return "redirect:/products/" + id + "/edit";
        }
    }

    /**
     * Delete a product.
     */
    @PostMapping("products/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            productService.deleteProduct(id);
            redirect.addFlashAttribute("success", "Product deleted");
            return "redirect:/products";
        } catch (Exception e) {
            log.error("Error deleting product", e);
            redirect.addFlashAttribute("error", "Failed to delete product");
            return "redirect:/products/" + id;
        }
    }

    /**
     * Show audit trail page.
     */
    @GetMapping("audit")
    public String auditTrail(Model model) {
        List<ProductHistory> trail = productService.getAuditTrail();
        model.addAttribute("trail", trail);
        return "audit";
    }

    /**
     * Time-travel query - show product as of date.
     */
    @GetMapping("products/{id}/history")
    public String productHistory(@PathVariable Long id,
                                 @RequestParam(required = false) String asOf,
                                 Model model) {
        Product current = productService.getProduct(id);
        if (current == null) {
            return "redirect:/products?error=notfound";
        }

        List<ProductHistory> history = productService.getProductHistory(id);

        // If asOf parameter provided, do time-travel query
        Product historical = null;
        if (asOf != null && !asOf.isEmpty()) {
            try {
                LocalDateTime timestamp = LocalDateTime.parse(asOf);
                historical = productService.getProductAsOf(id, timestamp);
            } catch (Exception e) {
                log.warn("Invalid timestamp format: {}", asOf);
            }
        }

        model.addAttribute("product", current);
        model.addAttribute("historicalProduct", historical);
        model.addAttribute("history", history);
        model.addAttribute("asOf", asOf);

        return "product-history";
    }

    /**
     * API endpoint - get all products as JSON.
     */
    @GetMapping("api/products")
    @ResponseBody
    public List<Product> apiProducts() {
        return productService.getAllProducts();
    }

    /**
     * API endpoint - get product as JSON.
     */
    @GetMapping("api/products/{id}")
    @ResponseBody
    public Product apiProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    /**
     * API endpoint - get statistics as JSON.
     */
    @GetMapping("api/stats")
    @ResponseBody
    public InventoryStats apiStats() {
        return productService.getStats();
    }

    /**
     * API endpoint - get product history as JSON.
     */
    @GetMapping("api/products/{id}/history")
    @ResponseBody
    public List<ProductHistory> apiHistory(@PathVariable Long id) {
        return productService.getProductHistory(id);
    }
}