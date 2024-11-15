package com.ikal.bookify.controller;

import com.ikal.bookify.dto.ApiResponse;
import com.ikal.bookify.dto.ClassRequest;
import com.ikal.bookify.dto.PurchaseRequest;
import com.ikal.bookify.model.Class;
import com.ikal.bookify.service.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Class", description = "Class Endpoint")
@RestController
@RequestMapping("/api/v1/class")
public class ClassController {

    @Autowired
    private ClassService classService;

    @Operation(
            description = "This is a Bookify API Available Classes Endpoint",
            summary = "Available Classes",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            }

    )
    @GetMapping
    public ResponseEntity<ApiResponse> getAvailableClasses(@RequestParam String country) {
        List<Class> classes = classService.getAvailableClasses(country);
        return ResponseEntity.ok(new ApiResponse("Success", "Get Aavailable Classes",classes));
    }

    @Operation(
            description = "This is a Bookify API Get Class Endpoint",
            summary = "Get Class",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Resource Not Found",
                            responseCode = "404",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            }

    )
    @GetMapping("/{classId}")
    public ResponseEntity<ApiResponse> getClassById(@PathVariable Long classId) {
        Class classEntity = classService.getClassById(classId);
        return ResponseEntity.ok(new ApiResponse("Success", "Get Class",classEntity));
    }

    @Operation(
            description = "This is a Bookify API Create Class Endpoint",
            summary = "Create Class",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(schema = @Schema(implementation = ClassRequest.class))
            )
    )
    @PostMapping
    public ResponseEntity<ApiResponse> createClass(@RequestBody ClassRequest classEntity) {
        Class newClass = classService.createClass(classEntity);
        return ResponseEntity.ok(new ApiResponse("Success", "Class is successfully created."));
    }

    @Operation(
            description = "This is a Bookify API Update Class Endpoint",
            summary = "Update Class",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = ClassRequest.class))
            )
    )
    @PutMapping("/{classId}")
    public ResponseEntity<ApiResponse> updateClass(@PathVariable Long classId, @RequestBody ClassRequest classDetails) {
        Class updatedClass = classService.updateClass(classId, classDetails);
        return ResponseEntity.ok(new ApiResponse("Success", "Class is successfully updated."));
    }

    @Operation(
            description = "This is a Bookify API Delete Class Endpoint",
            summary = "Delete Class",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            description = "Resource Not Found",
                            responseCode = "404",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
            }

    )
    @DeleteMapping("/{classId}")
    public ResponseEntity<ApiResponse> deleteClass(@PathVariable Long classId) {
        classService.deleteClass(classId);
        return ResponseEntity.ok(new ApiResponse("Success", "Class is successfully deleted."));
    }
}
