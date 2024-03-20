package com.ruleshake.samples.back.common;

import com.ruleshake.referential.client.dto.DatasetRecord;
import com.ruleshake.runner.client.input.InputVariable;
import com.ruleshake.runner.client.variable.Variable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GenericController {

    private final GenericService genericService;

    @GetMapping("/datasets/{datasetCode}/records")
    public Flux<DatasetRecord> listRecords(@PathVariable String datasetCode) {
        return genericService.listDatasetRecords(datasetCode);
    }

    @PostMapping("/collections/{collectionCode}/evaluations")
    public Flux<Variable> evaluations(@PathVariable String collectionCode, @RequestBody List<InputVariable> input) {
        return genericService.evaluate(collectionCode, input);
    }

}
