package com.bhasaka.farmfresh.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = { Resource.class,
        SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FeaturedProductsModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String[] productTags;

    @SlingObject
    ResourceResolver resolver;

    @ChildResource
    private List<CategoriesModel> categories;

    List<ProductModel> products;

    @PostConstruct
    public void init() {
        if (productTags != null && productTags.length > 0) {
            StringBuilder queryBuilder = new StringBuilder(
                    "SELECT * FROM [dam:Asset] AS s WHERE ISDESCENDANTNODE([/content/dam/farmFresh]) AND ");
    
            for (int i = 0; i < productTags.length; i++) {
                if (i > 0) {
                    queryBuilder.append(" OR ");
                }
                queryBuilder.append("s.[jcr:content/metadata/cq:tags] LIKE '%").append(productTags[i]).append("%'");
            }
            String query = queryBuilder.toString();
    
            Iterator<Resource> resources = resolver.findResources(query, "JCR-SQL2");
            List<ProductModel> products = new ArrayList<>();
            while (resources.hasNext()) {
                Resource resource = resources.next();
                Resource productResource = resolver.getResource(resource.getPath() + "/jcr:content/data/master");
                if (productResource != null) {
                    ProductModel product = productResource.adaptTo(ProductModel.class);
                    if (product != null) {
                        for (CategoriesModel category : categories) {
                            
                                product.setCategoryNames(category.getCategoryName());
                                break;
                            
                        }
                        products.add(product);
                    }
                }
            }
            this.products = products;
        }
    }

    // private boolean productTagsMatchCategory(ProductModel product, CategoriesModel category) {
    //     return product.getCategoryName() != null && product.getCategoryName().equalsIgnoreCase(category.getCategoryName());
    // }

    public List<ProductModel> getProducts() {
        return products;
    }

    public String getTitle() {
        return title;
    }

    public List<CategoriesModel> getCategories() {
        return categories;
    }

}