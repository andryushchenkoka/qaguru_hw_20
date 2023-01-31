package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersList {

    private Integer page;
    @JsonProperty("per_page")
    private Integer perPage;
    private Integer total;
    @JsonProperty("total_pages")
    private Integer totalPages;
    private ArrayList<User> data;
    private Support support;
}
