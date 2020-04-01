package com.movie.search.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class SelectParam {
    
    private String Index;
    
    private String type;
    
    private float boost;
    
    private Integer pageSize;
    
    private Long limit;
    
    private List<Param> selectParam = new ArrayList<>();
    
    private List<Param> filterParam = new ArrayList<>();
    
    @Data
    public static class Param {
        private String field;
        
        private String keyWord;
        
        private boolean must;
        
        public Param(String field, String keyWord, boolean must) {
            this.field = field;
            this.keyWord = keyWord;
            this.must = must;
        }
        
        public Param() {
        }
    }
}
