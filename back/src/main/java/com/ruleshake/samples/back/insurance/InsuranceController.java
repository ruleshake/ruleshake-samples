package com.ruleshake.samples.back.insurance;

import com.ruleshake.referential.client.dto.DatasetRecord;
import com.ruleshake.runner.client.input.InputVariable;
import com.ruleshake.runner.client.variable.Variable;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/insurance")
public class InsuranceController {

    private final InsuranceService insuranceService;

    @GetMapping("/dataset-records/{datasetCode}")
    public Flux<DatasetRecord> listRecords(@PathVariable String datasetCode) {
        return insuranceService.listDatasetRecords(datasetCode);
    }

    @PostMapping("/evaluations")
    public Flux<Variable> evaluations(@RequestBody List<InputVariable> input) {
        return insuranceService.evaluate(input);
    }

}
