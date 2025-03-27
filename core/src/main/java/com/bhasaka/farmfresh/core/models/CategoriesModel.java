package com.bhasaka.farmfresh.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CategoriesModel {

    @ValueMapValue
    private String categoryTitle;

    @ValueMapValue
    private String categoryName;

    @ValueMapValue
    private String categoryNames;

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryNames() {
        return categoryNames;
    }

}
