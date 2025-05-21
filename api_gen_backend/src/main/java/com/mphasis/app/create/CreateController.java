package com.mphasis.app.create;

import java.util.List;
import com.mphasis.app.support.SupportFunction;
import com.mphasis.app.model.Services;
import com.mphasis.app.model.Table;
import com.mphasis.app.model.Column;
import com.mphasis.app.model.Params;

public class CreateController {

    String name;
    String service;
    String serviceObj;
    String modelObj;
    String model;
    String toPackage;
    List<String> requirements;
    List<Services> services;
    List<Column> columns;

    StringBuilder javaStringBuilder = new StringBuilder();
    SupportFunction support = new SupportFunction();

    public CreateController(Table table) {
        this.name = table.getTableName();
        this.service = support.capitalizedName(this.name) + "Service";
        this.serviceObj = this.name + "Service";
        this.model = support.capitalizedName(this.name) + "Model";
        this.modelObj = this.name + "Model";
        this.requirements = table.getRequirement();
        this.toPackage = table.getToPackage();
        this.services = table.getServices();
        this.columns = table.getColumns();
    }

    private void addPackage() {
        javaStringBuilder.append("package " + this.toPackage + ".controller;\n\n");
    }

    private void addImports() {
        javaStringBuilder.append("import java.util.List;\n"
                + "import org.apache.logging.log4j.LogManager;\n"
                + "import org.apache.logging.log4j.Logger;\n"
                + "import org.springframework.beans.factory.annotation.Autowired;\n"
                + "import org.springframework.http.HttpStatus;\n"
                + "import org.springframework.http.ResponseEntity;\n"
                + "import org.springframework.web.bind.annotation.*;\n\n");
    }

    private void addCustomImports() {
        javaStringBuilder
                .append("import " + this.toPackage + ".service." + support.capitalizedName(this.serviceObj) + ";\n"
                        + "import " + this.toPackage + ".model." + support.capitalizedName(this.modelObj) + ";\n");
    }

    private void addBoilerPlate() {
        javaStringBuilder.append("@RestController\n"
                + "@RequestMapping(\"/" + support.getBasePackage(toPackage) + "/api/v1/" + this.name.toLowerCase()
                + "\")\n"
                + "public class " + support.capitalizedName(this.name) + "Controller {\n");
    }

    private void addServiceClass() {
        javaStringBuilder.append("    @Autowired\n"
                + "    private " + this.service + " " + this.serviceObj + ";\n");
    }

    private void addLogger() {
        javaStringBuilder.append("    private Logger log = LogManager.getLogger(getClass());\n");
    }

    private void addStandardMethods() {
        for (String req : this.requirements) {
            switch (req) {
                case "getById":
                    addGetByIdService();
                    break;
                case "getAll":
                    addGetAllService();
                    break;
                case "add":
                    addAddEntityService();
                    break;
                case "count":
                    addGetCountService();
                    break;
                case "update":
                    System.out.println("yet to add update");
                    break;
                default:
                    System.out.println("requirement: [" + req + "] cannot be satisfied");
            }
        }
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

    private String getMappingAnnotations(Services sf, String methodName) {
        return "\n    " + support.getMappingAnnotations(sf.getMapping()) + "(\"" +
                buildSpecialFunctionEndpoint(sf) + "\")\n" +
                "    public ResponseEntity<?>" + methodName;
    }

    private String addInputFields(Services sf) {
        StringBuilder fieldBuilder = new StringBuilder();
        List<String> fields = sf.getField();
        fieldBuilder.append("(");
        for (int i = 0; i < fields.size(); i++) {
            String field = fields.get(i);
            fieldBuilder.append(support.toCamelCase(field));
            if (i < fields.size() - 1) {
                fieldBuilder.append(", ");
            }
        }
        fieldBuilder.append(");\n");
        return fieldBuilder.toString();
    }

    private String addFieldsForMethod(Services sf) {
        StringBuilder fieldBuilder = new StringBuilder();
        List<String> fields = sf.getField();
        for (int i = 0; i < fields.size() ; i++) {
            String field = fields.get(i);
            fieldBuilder.append(support.capitalizedName(support.toCamelCase(field)));
            
            if(i != fields.size() - 1)
            {
                fieldBuilder.append("And");
            }
        }
        return fieldBuilder.toString();
    }

    private String addParameters(Services sf) {
        List<Params> parmList = sf.getParams();
        StringBuilder fieldBuilder = new StringBuilder();
    
        for (int i = 0; i < parmList.size(); i++) {
            Params param = parmList.get(i);
            fieldBuilder.append(support.getVariableAnnotations(param.getParamRequestType()))
                    .append(" ")
                    .append(param.getParamType())
                    .append(" ")
                    .append(param.getParamName());
    
            if (i < parmList.size() - 1) {
                fieldBuilder.append(", ");
            }
        }
    
        return fieldBuilder.toString();
    }

    private String addOrder(Services sf) {
        if(sf.getOrderBy().equals(""))
        {
            return "OrderBy" + sf.getOrderBy();
        }
        else{
            return "";
        }
    }
    

    private void addSpecialFunctionHandlers() {
        if (services == null || services.isEmpty())
            return;

        for (Services sf : services) {
            String methodName = support.repoConvertion(sf.getAction());
            String returnType = sf.getReturnType();

            javaStringBuilder.append(getMappingAnnotations(sf, methodName));
            javaStringBuilder.append(addFieldsForMethod(sf));
            javaStringBuilder.append("(");
            javaStringBuilder.append(addParameters(sf));

            javaStringBuilder.append(") {\n")
                    .append("        try {\n")
                    .append("            ").append(returnType).append(" result = ")
                    .append(this.serviceObj).append(".").append(methodName).append(addFieldsForMethod(sf)).append(addOrder(sf))
                    .append(addInputFields(sf))
                    .append("            return ResponseEntity.status(HttpStatus.OK).body(result);\n")
                    .append("        }").append(addStandardExceptionHandling()).append("\n")
                    .append("    }\n");
        }
    }

    private String buildSpecialFunctionEndpoint(Services sf) {
        StringBuilder sb = new StringBuilder("/");
        sb.append(sf.getAction().toLowerCase());
        

        if (!sf.getParams().isEmpty() & sf.getParams().get(0).getParamRequestType().equals("path")) {
            sb.append("/by");
            for (String field : sf.getField()) {
                sb.append("/").append(support.toCamelCase(field)).append("/{" + support.toCamelCase(field) + "}");
            }
        }

        return sb.toString();
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

    public String GetString() {
        addPackage();
        addImports();
        addCustomImports();
        addBoilerPlate();
        addServiceClass();
        addLogger();
        addStandardMethods();
        addSpecialFunctionHandlers();
        javaStringBuilder.append("\n}");
        return javaStringBuilder.toString();
    }
}
