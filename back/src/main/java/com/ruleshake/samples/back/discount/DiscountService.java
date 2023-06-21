package com.ruleshake.samples.back.discount;

import com.ruleshake.core.util.VariableReferenceUtil;
import com.ruleshake.referential.client.ReferentialClient;
import com.ruleshake.referential.client.dto.DatasetRecord;
import com.ruleshake.runner.client.RunnerClient;
import com.ruleshake.runner.client.collection.EvaluationRequest;
import com.ruleshake.runner.client.collection.EvaluationResponse;
import com.ruleshake.runner.client.input.InputRecordVariable;
import com.ruleshake.runner.client.input.InputVariable;
import com.ruleshake.runner.client.variable.CompositeVariable;
import com.ruleshake.runner.client.variable.NumberVariable;
import com.ruleshake.samples.back.common.AuthService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class DiscountService {

    private final AuthService authService;
    private final ReferentialClient referentialClient;
    private final RunnerClient runnerClient;

    public Flux<Article> listArticles() {
        return authService.getSecurityContext()
            .flatMapMany(context -> referentialClient.dataset("ARTICLES")
                .records()
                .search(Map.of())
                .contextWrite(c -> context)
                .flatMapMany(response -> Flux.fromIterable(response.content()))
                .map(this::mapToArticle)
            );
    }

    private Article mapToArticle(DatasetRecord datasetRecord) {
        Article article = new Article();
        article.setBarcode(datasetRecord.getCode());
        article.setLabel((String) datasetRecord.getProperties().get("LABEL").value());
        article.setCategory((String) datasetRecord.getProperties().get("CATEGORY").value());
        article.setImage((String) datasetRecord.getProperties().get("IMAGE").value());
        article.setPrice(Double.valueOf((String) datasetRecord.getProperties().get("PRICE").value()));
        return article;
    }

    public Flux<Discount> applyDiscounts(List<Article> cart) {
        return authService.getSecurityContext()
            .flatMap(context -> runnerClient.evaluations()
                .evaluate(buildRequest(cart))
                .contextWrite(c -> context)
            )
            .map(this::parseResponse)
            .flatMapMany(Flux::fromIterable);
    }

    private EvaluationRequest buildRequest(List<Article> cart) {
        EvaluationRequest request = new EvaluationRequest();
        request.setCollectionCode("DISCOUNT");
        request.setRequestTime(LocalDateTime.now());
        List<InputVariable> inputs = IntStream.range(0, cart.size())
            .mapToObj(index -> {
                InputRecordVariable recordVariable = new InputRecordVariable();
                recordVariable.setReference(VariableReferenceUtil.buildReference(null, "ARTICLE", index));
                recordVariable.setValue(cart.get(index).getBarcode());
                return recordVariable;
            })
            .collect(Collectors.toList());
        request.setInputs(inputs);
        return request;
    }

    private List<Discount> parseResponse(EvaluationResponse response) {
        return response.getVariables()
            .stream()
            .filter(variable -> variable.getRuntimeReference().equals("DISCOUNTS"))
            .map(CompositeVariable.class::cast)
            .flatMap(discounts -> discounts.getSubVariables().stream())
            .map(NumberVariable.class::cast)
            .map(variable -> new Discount(
                VariableReferenceUtil.getCode(variable.getDefinitionReference()),
                variable.getProperties().get("DESC"),
                variable.getValue().doubleValue()
            ))
            .toList();
    }
}
