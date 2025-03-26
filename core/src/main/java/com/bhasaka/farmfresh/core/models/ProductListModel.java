package com.bhasaka.farmfresh.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
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
public class ProductListModel {

    @ValueMapValue
    private String categoryTag;

    @SlingObject
    private ResourceResolver resolver;

     private List<String[]> productList =new ArrayList<>();

    @PostConstruct
    protected void init() {
        if (Objects.nonNull(resolver) && categoryTag != null && !categoryTag.isEmpty()) {
            String query = "SELECT * FROM [dam:Asset] AS asset " + "WHERE asset.[jcr:content/metadata/cq:tags]" +
                           " = '" + categoryTag + "' " +"AND ISDESCENDANTNODE(asset, '/content/dam/farmFresh')";
            Iterator<Resource> results = resolver.findResources(query, "JCR-SQL2");
            while (results.hasNext()) {
                Resource assetResource = results.next();
                if (Objects.nonNull(assetResource)) {
                    String cfPath = assetResource.getPath() + "/jcr:content/data/master";
                    Resource cfResource = resolver.getResource(cfPath);
                    if (cfResource != null) {
                        ValueMap props = cfResource.getValueMap();
                        String productImage = props.get("productImage", String.class);
                        String productTitle = props.get("productTitle", String.class);
                        productList.add(new String[]{productImage,productTitle});
                    }
                }
            }
        }
    }

    public List<String[]> getProductList() {
        return productList;
    }
}
