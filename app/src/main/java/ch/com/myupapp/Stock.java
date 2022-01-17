package ch.com.myupapp;

import org.json.JSONArray;

public class Stock {
    private String title;
    private int change;
    private String companyName;
    private JSONArray JsonChartData;

    public JSONArray getJsonChartData() {
        return JsonChartData;
    }

    public void setJsonChartData(JSONArray jsonChartData) {
        JsonChartData = jsonChartData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
