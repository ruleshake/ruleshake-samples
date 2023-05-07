package com.ruleshake.samples.back.discount;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/discount")
public class DiscountController {

    private final DiscountService discountService;

    @GetMapping("/articles")
    public Flux<Article> listArticles() {
        return discountService.listArticles();
    }

    @PostMapping("/apply")
    public Flux<Discount> applyDiscounts(@RequestBody List<Article> cart) {
        return discountService.applyDiscounts(cart);
    }

}
