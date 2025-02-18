package com.laacrm.main.core.controller.record;

import com.laacrm.main.core.controller.APIController;
import com.laacrm.main.core.entity.Record;
import com.laacrm.main.core.service.RecordService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api")
@AllArgsConstructor
public class RecordController extends APIController {

    private final RecordService recordService;

    @GetMapping("/{moduleId}/record")
    public ResponseEntity<APIResponse> getAllRecords(@PathVariable(name = "moduleId") Long moduleId){
        Map<String, Object> details = new HashMap<>();
        List<RecordDTO> recordDTOS = new ArrayList<>();
        for(Record record : recordService.getAllRecordsForModule(moduleId)){
            recordDTOS.add(getRecordDTOFromEntity(record));
        }
        details.put("records", recordDTOS);
        addResponse(HttpStatus.OK.value(), "Records fetched successfully", details);
        return response();
    }

    @GetMapping("{moduleId}/record/{recordId}")
    public ResponseEntity<APIResponse> getRecordById(@PathVariable(name = "moduleId") Long moduleId, @PathVariable("recordId") Long recordId){
        Record record = recordService.getRecordForModuleById(moduleId, recordId);
        if(record != null){
            Map<String, Object> details = new HashMap<>();
            details.put("record", getRecordDTOFromEntity(record));
            addResponse(HttpStatus.OK.value(), "Record fetched successfully", details);
        }else {
            addResponse(HttpStatus.NOT_FOUND.value(), "Record not found");
        }
        return response();
    }

    @PostMapping("{moduleId}/record")
    public ResponseEntity<APIResponse> saveRecord(@RequestBody RecordDTO recordDTO){
        Record record = getRecordEntityFromDTO(recordDTO);
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());
        record = recordService.saveRecord(record);
        Map<String, Object> details = new HashMap<>();
        details.put("recordId", record.getRecordId().toString());
        addResponse(HttpStatus.OK.value(), "Record saved successfully", details);
        return response();
    }

    @PutMapping("/{moduleId}/record")
    public ResponseEntity<APIResponse> updateRecord(@RequestBody RecordDTO recordDTO){
        Record record = getRecordEntityFromDTO(recordDTO);
        record.setUpdatedAt(LocalDateTime.now());
        record = recordService.saveRecord(record);
        Map<String, Object> details = new HashMap<>();
        details.put("recordId", record.getRecordId().toString());
        addResponse(HttpStatus.OK.value(), "Record updated successfully", details);
        return response();
    }

    @DeleteMapping("/{moduleId}/record/{recordId}")
    public ResponseEntity<APIResponse> deleteRecord(@PathVariable(name = "moduleId") Long moduleId, @PathVariable(name = "recordId") Long recordId){
        recordService.deleteRecordById(moduleId, recordId);
        Map<String, Object> details = new HashMap<>();
        details.put("recordId", recordId.toString());
        addResponse(HttpStatus.OK.value(), "Record deleted successfully", details);
        return response();
    }

    private Record getRecordEntityFromDTO(RecordDTO recordDTO){
        return new Record(
                recordDTO.getRecordId() != null && !recordDTO.getRecordId().isEmpty() ? Long.valueOf(recordDTO.getRecordId()) : null,
                recordDTO.getModuleId() != null && !recordDTO.getModuleId().isEmpty() ? Long.valueOf(recordDTO.getModuleId()) : null,
                recordDTO.getLayoutId() != null && !recordDTO.getLayoutId().isEmpty() ? Long.valueOf(recordDTO.getLayoutId()) : null,
                recordDTO.getRecordDetails()
        );
    }

    private RecordDTO getRecordDTOFromEntity(Record record){
        return new RecordDTO(
                String.valueOf(record.getRecordId()),
                String.valueOf(record.getModuleId()),
                String.valueOf(record.getLayoutId()),
                record.getCreatedAt().toString(),
                record.getUpdatedAt().toString(),
                record.getRecordDetails()
        );
    }

}
