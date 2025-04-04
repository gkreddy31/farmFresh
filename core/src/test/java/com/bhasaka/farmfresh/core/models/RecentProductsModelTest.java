package com.bhasaka.farmfresh.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class RecentProductsModelTest {

    private final AemContext context = new AemContext();

    @Mock
    private ResourceResolver mockResolver;

    @Mock
    private Resource mockResultResource;

    @Mock
    private Resource mockProductResource;

    @Mock
    private ProductModel mockProductModel;

    private RecentProductsModel recentProductsModel;

    @BeforeEach
    void setup() {
        context.create().resource("/content/dam/farmFresh",
                "title", "Latest Products",
                "categoryTag", new String[]{"electronics", "new-arrivals"});

        Resource currentResource = context.resourceResolver().getResource("/content/dam/farmFresh");

        recentProductsModel = currentResource.adaptTo(RecentProductsModel.class);
        injectPrivateField("resolver", mockResolver, recentProductsModel);
    }

    @Test
    void testInitAndSubModelsList() {
        when(mockResolver.findResources(anyString(), eq("JCR-SQL2")))
                .thenReturn(List.of(mockResultResource).iterator());

        when(mockResultResource.getPath()).thenReturn("/content/dam/farmFresh/product1");
        when(mockResolver.getResource("/content/dam/farmFresh/product1/jcr:content/data/master"))
                .thenReturn(mockProductResource);
        when(mockProductResource.adaptTo(ProductModel.class)).thenReturn(mockProductModel);

        recentProductsModel.init();

        assertEquals("Latest Products", recentProductsModel.getTitle());
        List<List<ProductModel>> subModels = recentProductsModel.getSubModelsList();
        assertEquals(1, subModels.size());
        assertEquals(1, subModels.get(0).size());
        assertSame(mockProductModel, subModels.get(0).get(0));
    }

    private void injectPrivateField(String fieldName, Object value, Object target) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            fail("Failed to inject field: " + fieldName, e);
        }
    }
}
