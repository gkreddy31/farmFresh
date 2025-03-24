package com.bhasaka.farmfresh.core.models;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductCategoryModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String categoryTag;

    @SlingObject
    private ResourceResolver resolver;

    private final List<String[]> categoryList = new ArrayList<>();

    @PostConstruct
    public void init() {
        if (Objects.nonNull(categoryTag) && Objects.nonNull(resolver)) {
            String query = "SELECT * FROM [cq:Page] AS s " + "WHERE ISDESCENDANTNODE(s, '/content/farmFresh') " +
                             "AND s.[jcr:content/cq:tags] = '" + categoryTag + "'";
            Iterator<Resource> results = resolver.findResources(query, "JCR-SQL2");
            while (results.hasNext()){
               Resource pageResource = results.next();
               if(Objects.nonNull(pageResource)){
                 Page page = pageResource.adaptTo(Page.class);
                   if (page != null) {
                       categoryList.add(new String[]{page.getPath(), page.getTitle() != null ? page.getTitle() : ""});
                   }
               }
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public List<String[]> getCategoryList() {
        return categoryList;
    }
}
