package com.bhasaka.farmfresh.core.models;

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

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class RecentProductsModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String[] categoryTag;

    @SlingObject
    private ResourceResolver resolver;

    private final List<ProductModel> productModels = new ArrayList<>();
    private final List<List<ProductModel>> subModelsList = new ArrayList<>();

    @PostConstruct
    protected void init() {
        if (categoryTag != null && categoryTag.length > 0) {
            String query = buildQuery(categoryTag);
            Iterator<Resource> resources = resolver.findResources(query, "JCR-SQL2");
            while (resources.hasNext()) {
                Resource resource = resources.next();
                Resource productResource = resolver.getResource(resource.getPath() + "/jcr:content/data/master");
                if (productResource != null) {
                    ProductModel product = productResource.adaptTo(ProductModel.class);
                    if (product != null) {
                        productModels.add(product);
                    }
                }
            }
        }
        for( int i=0;i<productModels.size(); i+=3){
            subModelsList.add(productModels.subList(i,Math.min(i + 3, productModels.size())));
        }
    }


    private String buildQuery(String[] tags) {
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT * FROM [dam:Asset] AS s WHERE ISDESCENDANTNODE([/content/dam/farmFresh]) AND (");
        for (int i = 0; i < tags.length; i++) {
            if (i > 0) {
                queryBuilder.append(" OR ");
            }
            queryBuilder.append("s.[jcr:content/metadata/cq:tags] LIKE '%").append(tags[i]).append("%'");
        }
        queryBuilder.append(")");
        return queryBuilder.toString();
    }

    public String getTitle() {
        return title;
    }

    public List<List<ProductModel>> getSubModelsList() {
        return subModelsList;
    }
}
