package com.example.spring_eshop.endpoint;


import com.example.spring_eshop.dto.ProductDTO;
import com.example.spring_eshop.service.ProductsService;
import com.example.spring_eshop.ws.products.GetProductsRequest;
import com.example.spring_eshop.ws.products.GetProductsResponse;
import com.example.spring_eshop.ws.products.ProductsWS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;

@Endpoint
public class ProductsEndpoint {
    public static final String NAMESPACE_PRODUCTS = "http://example.com/spring_eshop/ws/products";
    private final ProductsService productsService;

    @Autowired
    public ProductsEndpoint(ProductsService productsService) {
        this.productsService = productsService;
    }

    // как RequestMapping
    @PayloadRoot(namespace = NAMESPACE_PRODUCTS, localPart = "getProductsRequest")
    @ResponsePayload // полезная нагрузка
    public GetProductsResponse getProductsWS(@RequestPayload GetProductsRequest request) throws DatatypeConfigurationException {
        GetProductsResponse response = new GetProductsResponse();
        productsService.getAll()
                .forEach(dto -> response.getProducts().add(createProductsWS(dto)));
        return response;
    }

    private ProductsWS createProductsWS(ProductDTO dto) {
        ProductsWS ws = new ProductsWS();
        ws.setId(dto.getId());
        ws.setPrice(Double.parseDouble(dto.getPrice().toString()));
        ws.setTitle(dto.getTitle());
        return ws;
    }
}
