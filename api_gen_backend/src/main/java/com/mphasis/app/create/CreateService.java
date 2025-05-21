package com.mphasis.app.create;

import java.util.List;

import com.mphasis.app.model.Column;
import com.mphasis.app.model.Params;
import com.mphasis.app.model.Services;
import com.mphasis.app.model.Table;
import com.mphasis.app.support.SupportFunction;

public class CreateService {

    String name;
    String repo;
    String repoObj;
    String model;
    String modelObj;
    String toPackage;
    List<String> requirements;
    List<Services> services;
    List<Column> columns;
    StringBuilder javaStringBuilder = new StringBuilder();
    SupportFunction support = new SupportFunction();

    public CreateService(Table table) {
        this.name = table.getTableName();
        this.repo = support.capitalizedName(this.name) + "Repo";
        this.repoObj = this.name + "Repo";
        this.model = support.capitalizedName(this.name) + "Model";
        this.modelObj = this.name + "Model";
        this.requirements = table.getRequirement();
        this.services = table.getServices();
        this.toPackage = table.getToPackage();
        this.columns = table.getColumns();
    }

    private void addPackage() {
        javaStringBuilder.append("package " + this.toPackage + "." + this.repoObj + ";\n\n");
    }

    private void addImports() {
        javaStringBuilder.append(
            "import jakarta.validation.Validator;\n" +
            "import jakarta.validation.ConstraintViolation;\n" +
            "import java.util.List;\n" +
            "import java.util.Optional;\n" +
            "import java.util.Set;\n" +
            "import org.apache.logging.log4j.LogManager;\n" +
            "import org.apache.logging.log4j.Logger;\n" +
            "import org.springframework.beans.factory.annotation.Autowired;\n" +
            "import org.springframework.data.domain.PageRequest;\n" +
            "import org.springframework.data.domain.Pageable;\n" +
            "import org.springframework.stereotype.Service;\n"
        );
    }

    private void addClassDeclaration() {
        javaStringBuilder.append("@Service\n");
        javaStringBuilder.append("public class " + support.capitalizedName(this.name) + "Service {\n\n");
    }

    private void addFields() {
        javaStringBuilder.append("    @Autowired\n    private " + repo + " " + repoObj + ";\n\n");
        javaStringBuilder.append("    private Logger log = LogManager.getLogger(getClass());\n\n");
        javaStringBuilder.append("    @Autowired\n    private Validator validator;\n\n");
    }

    private void addValidationHelpers() {
        javaStringBuilder.append(
            "    private void checkForNull(" + model + " model) throws CrudOperationException {\n" +
            "        if (model == null) {\n" +
            "            throw CrudOperationException.asNullEntity(" + model + ".class);\n" +
            "        }\n" +
            "    }\n\n"
        );

        javaStringBuilder.append(
            "    private void validate(" + model + " model) throws CrudValidationException {\n" +
            "        Set<ConstraintViolation<" + model + ">> violations = validator.validate(model);\n" +
            "        if (!violations.isEmpty()) {\n" +
            "            throw CrudValidationException.asFailedValidationOperation(" + model + ".class, violations);\n" +
            "        }\n" +
            "    }\n\n"
        );

        javaStringBuilder.append(
            "    private void checkId(long id) throws CrudValidationException {\n" +
            "        if (id <= 0) {\n" +
            "            throw CrudValidationException.asInvalidEntityId(" + model + ".class);\n" +
            "        }\n" +
            "    }\n\n"
        );
    }

    private void addSaveHelper() {
        javaStringBuilder.append(
            "    private " + model + " save(" + model + " model) throws CrudOperationException {\n" +
            "        try {\n" +
            "            boolean isNew = model.getId() <= 0;\n" +
            "            " + model + " saved = " + repoObj + ".save(model);\n" +
            "            log.info((isNew ? \"Added\" : \"Updated\") + \" " + model + " with ID: \" + saved.getId());\n" +
            "            return saved;\n" +
            "        } catch (Exception e) {\n" +
            "            throw CrudOperationException.asFailedAddOperation(" + model + ".class, e);\n" +
            "        }\n" +
            "    }\n\n"
        );
    }

    private void addGetAllMethod() {
        javaStringBuilder.append(
            "    public List<" + model + "> getAll(int page, int size) throws CrudOperationException {\n" +
            "        try {\n" +
            "            Pageable pageable = PageRequest.of(\n" +
            "                page < ServiceConstants.STARTING_PAGE_NUMBER ? ServiceConstants.STARTING_PAGE_NUMBER : page,\n" +
            "                size <= 0 ? ServiceConstants.DEFAULT_ITEMS_PER_PAGE : size);\n" +
            "            return " + repoObj + ".findAll(pageable).getContent();\n" +
            "        } catch (Exception e) {\n" +
            "            throw CrudOperationException.asFailedGetOperation(getClass(), e);\n" +
            "        }\n" +
            "    }\n\n"
        );
    }

    private void addGetByIdMethod() {
        javaStringBuilder.append(
            "    public " + model + " getById(Long id) throws CrudOperationException, CrudValidationException {\n" +
            "        checkId(id);\n" +
            "        Optional<" + model + "> result = " + repoObj + ".findById(id);\n" +
            "        checkForNull(result.get());\n" +
            "        return result.get();\n" +
            "    }\n\n"
        );
    }

    private void addAddMethod() {
        javaStringBuilder.append(
            "    public " + model + " addNew" + model + "(" + model + " model) throws CrudValidationException, CrudOperationException {\n" +
            "        checkForNull(model);\n" +
            "        validate(model);\n" +
            "        return save(model);\n" +
            "    }\n\n"
        );
    }

    public void addUpdateMethod() {
        javaStringBuilder.append(
            "    public " + model + " update" + model + "(" + model + " model) throws CrudValidationException, CrudOperationException {\n" +
            "        checkForNull(model);\n" +
            "        Optional<" + model + "> existing = " + repoObj + ".findById(model.getId());\n" +
            "        if (existing.isEmpty()) {\n" +
            "            throw CrudOperationException.asEntityNotFound(" + model + ".class, model.getId());\n" +
            "        }\n" +
            "        model.setCreatedAt(existing.get().getCreatedAt());\n" +
            "        model.setUpdatedAtNow();\n" +
            "        validate(model);\n" +
            "        return save(model);\n" +
            "    }\n\n"
        );
    }

    private void addCountMethod() {
        javaStringBuilder.append(
            "    public long getCountOf" + name + "s() throws CrudOperationException {\n" +
            "        try {\n" +
            "            return " + repoObj + ".count();\n" +
            "        } catch (Exception e) {\n" +
            "            throw CrudOperationException.asFailedGetOperation(" + model + ".class, e);\n" +
            "        }\n" +
            "    }\n\n"
        );
    }

    private String addParameters(Services sf) {
        List<Params> parmList = sf.getParams();
        StringBuilder fieldBuilder = new StringBuilder();
    
        for (int i = 0; i < parmList.size(); i++) {
            Params param = parmList.get(i);
            fieldBuilder
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
    
    private String addFields(Services sf)
    {
        List<Params> parmList = sf.getParams();
        StringBuilder fieldBuilder = new StringBuilder();

        for(Params param : parmList)
        {
            fieldBuilder.append(" " + param.getParamType() + " " + param.getParamName());
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

    private void addSpecialFunctionMethods() {
        for (Services sf : services) {
            String methodName = support.repoConvertion(sf.getAction()) + support.getServiceFunctionName(sf.getField()) + addOrder(sf);
            String returnType = sf.getReturnType();
            javaStringBuilder.append("    public " + returnType + " " + methodName + "(");
            javaStringBuilder.append(addParameters(sf));
            javaStringBuilder.append(") throws CrudOperationException {\n");
            javaStringBuilder.append("        try {\n");
            javaStringBuilder.append("            return " + repoObj + "." + methodName + "(");

            javaStringBuilder.append(addFields(sf));

            javaStringBuilder.append(");\n");
            javaStringBuilder.append("        } catch (Exception e) {\n");
            javaStringBuilder.append("            throw CrudOperationException.asFailedGetOperation(" + model + ".class, e);\n");
            javaStringBuilder.append("        }\n");
            javaStringBuilder.append("    }\n\n");
        }
    }

    private void addRequierments() {
        for (String req : this.requirements) {
            switch (req) {
                case "getById":
                    addGetByIdMethod();
                    break;
                case "getAll":
                    addGetAllMethod();
                    break;
                case "add":
                    addAddMethod();
                    break;
                case "count":
                    addCountMethod();
                    break;
                case "update":
                    addUpdateMethod();
                    break;
                default:
                    System.out.println("requierment : [" + req + "] cannot be statisfied");
            }
        }
    }

    public String getString() {
        addPackage();
        addImports();
        addClassDeclaration();
        addFields();
        addValidationHelpers();
        addSaveHelper();
        addRequierments();
        addSpecialFunctionMethods();
        javaStringBuilder.append("}\n");
        return javaStringBuilder.toString();
    }
}
