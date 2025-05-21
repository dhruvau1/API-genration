package com.mphasis.app.create;

import com.mphasis.app.model.Table;
import com.mphasis.app.model.Params;
import com.mphasis.app.model.Services;
import com.mphasis.app.support.SupportFunction;

import java.util.List;

public class CreateRepo {

    private final String modelName;
    private final String toPackage;
    private final List<Services> services;
    private final Table table;
    private final SupportFunction support = new SupportFunction();

    public CreateRepo(Table table) {
        this.modelName = support.capitalizedName(table.getTableName());
        this.toPackage = table.getToPackage();
        this.services = table.getServices();
        this.table = table;
    }

    public String GetString() {
        return "package " + this.toPackage + ".repository;\n\n"
                + addImports() + "\n"
                + "public interface " + this.modelName + "Repo extends JpaRepository<"
                + this.modelName + "Model, Long> {\n"
                + addservices(this.services, this.table)
                + "}\n";
    }

    private String addImports() {
        return "import org.springframework.data.jpa.repository.JpaRepository;\n"
                + "import " + this.toPackage + ".model." + this.modelName + "Model;\n";
    }

    private String addFieldsForMethod(Services sf) {
        StringBuilder fieldBuilder = new StringBuilder();
        List<String> fields = sf.getField();
        for (int i = 0; i < fields.size(); i++) {
            String field = fields.get(i);
            fieldBuilder.append(support.capitalizedName(support.toCamelCase(field)));

            if (i != fields.size() - 1) {
                fieldBuilder.append("And");
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

    private String addservices(List<Services> services, Table table) {

        if (services == null || services.isEmpty())
            return "";

        StringBuilder javaStringBuilder = new StringBuilder();

        for (Services sf : services) {
            String methodName = support.repoConvertion(sf.getAction());
            String returnType = sf.getReturnType();

            javaStringBuilder.append(returnType).append(" ");
            javaStringBuilder.append(methodName).append(addFieldsForMethod(sf)).append(addOrder(sf));
            javaStringBuilder.append("(");
            javaStringBuilder.append(addParameters(sf)).append(");\n");
        }

        return javaStringBuilder.toString();

    }

}
