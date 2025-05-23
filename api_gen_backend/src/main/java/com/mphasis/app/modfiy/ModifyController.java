package com.mphasis.app.modfiy;

import java.io.*;
import java.nio.file.*;

import com.mphasis.app.support.SupportFunction;

public class ModifyController {

    StringBuilder javaStringBuilder = new StringBuilder();
    private String model;
    private String serviceObj;
    private String modelObj;
    private String name;
    SupportFunction support = new SupportFunction();
    
    public ModifyController(String model) {
        
        this.model = support.capitalizedName(model) + "Model";;
        this.serviceObj = this.name + "Service";;
        this.modelObj = this.name + "Model";;
        this.name = model;
    }

    private String addStandardExceptionHandling() {
        return "\n        catch (CrudValidationException e) {\n"
                + "            log.error(e.getMessage(), e);\n"
                + "            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());\n"
                + "        }\n"
                + "        catch (CrudOperationException e) {\n"
                + "            log.error(e.getMessage(), e);\n"
                + "            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());\n"
                + "        }";
    }

    private void addGetAllService() {
        javaStringBuilder.append("\n    @GetMapping(\"/get/all/page/{page}/size/{size}\")\n"
                + "    public ResponseEntity<?> getAll(@RequestParam(defaultValue = \"0\") int page,\n"
                + "                                    @RequestParam(defaultValue = \"10\") int size) {\n"
                + "        try {\n"
                + "            List<" + this.model + "> items = " + this.serviceObj + ".getAll(page, size);\n"
                + "            if (items.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(\"No records found.\");\n"
                + "            return ResponseEntity.ok(items);\n"
                + "        }" + addStandardExceptionHandling() + "\n    }\n");
    }

    private void addGetByIdService() {
        javaStringBuilder.append("\n    @GetMapping(\"/get/by/id/{id}\")\n"
                + "    public ResponseEntity<?> getById(@PathVariable long id) {\n"
                + "        if (id <= 0) return ResponseEntity.badRequest().body(\"Invalid ID\");\n"
                + "        try {\n"
                + "            return ResponseEntity.ok(" + this.serviceObj + ".getById(id));\n"
                + "        }" + addStandardExceptionHandling() + "\n    }\n");
    }

    private void addAddEntityService() {
        javaStringBuilder.append("\n    @PostMapping(\"/add\")\n"
                + "    public ResponseEntity<?> addNew(@RequestBody " + this.model + " " + this.modelObj.toLowerCase()
                + ") {\n"
                + "        if (" + this.modelObj.toLowerCase() + " == null)\n"
                + "            return ResponseEntity.badRequest().body(\"Invalid model\");\n"
                + "        try {\n"
                + "            return ResponseEntity.ok(" + this.serviceObj + ".addNew" + this.model + "("
                + this.modelObj.toLowerCase() + "));\n"
                + "        }" + addStandardExceptionHandling() + "\n    }\n");
    }

    private void addGetCountService() {
        javaStringBuilder.append("\n    @GetMapping(\"/get/count\")\n"
                + "    public ResponseEntity<?> getCount() {\n"
                + "        try {\n"
                + "            return ResponseEntity.ok(" + this.serviceObj + ".getCountOf" + this.name + "s());\n"
                + "        }" + addStandardExceptionHandling() + "\n    }\n");
    }

    private void addRequierment(String requirement) {
        
        switch (requirement) {
            case "getAll":
                addGetAllService();
                break;
            case "getById":
                addGetByIdService();
                break;
            case "addNew":
                addAddEntityService();
                break;
            case "getCount":
                addGetCountService();
                break;
            default:
                System.out.println("Invalid requirement");
        }
    }

    public void modify(String path, String requierment) throws IOException {
        
        addRequierment(requierment);
        String content = new String(Files.readAllBytes(Paths.get(path)));
        int insertIndex = content.lastIndexOf('}');
        if (insertIndex != -1) {
            content = content.substring(0, insertIndex) + javaStringBuilder.toString() + content.substring(insertIndex);
        }

    }
    
}
