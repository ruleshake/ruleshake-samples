package com.ruleshake.samples.back.insurance;

import com.ruleshake.referential.client.ReferentialClient;
import com.ruleshake.referential.client.dto.DatasetRecord;
import com.ruleshake.runner.client.RunnerClient;
import com.ruleshake.runner.client.collection.EvaluationRequest;
import com.ruleshake.runner.client.input.InputVariable;
import com.ruleshake.runner.client.variable.Variable;
import com.ruleshake.samples.back.common.AuthService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Collection;

@RequiredArgsConstructor
public class InsuranceService {

    private final AuthService authService;
    private final ReferentialClient referentialClient;
    private final RunnerClient runnerClient;

    public Flux<DatasetRecord> listDatasetRecords(String datasetCode) {
        return authService.getSecurityContext()
            .flatMapMany(context -> referentialClient.dataset(datasetCode)
                .records()
                .search(null)
                .flatMapMany(datasetRecordsPage -> Flux.fromIterable(datasetRecordsPage.content()))
                .contextWrite(context)
            );
    }

    public Flux<Variable> evaluate(Collection<InputVariable> inputs) {
        EvaluationRequest request = new EvaluationRequest();
        request.setRequestTime(LocalDateTime.now());
        request.setCollectionCode("ASSURANCE_AUTO");
        request.setInputs(inputs);
        return authService.getSecurityContext()
            .flatMapMany(context -> runnerClient.evaluations()
                .evaluate(request)
                .flatMapMany(response -> Flux.fromIterable(response.getVariables()))
                .contextWrite(context)
            );
    }

}
