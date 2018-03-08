package com.company.linebot.domain.repository.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.company.linebot.domain.Product;
import com.company.linebot.domain.repository.ProductRepository;
import com.sun.javafx.scene.control.skin.VirtualFlow.ArrayLinkedList;

@Repository
public class InMemoryProductRepository implements ProductRepository {


    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<Product> getAllProducts() {
        String SQL = "SELECT * FROM PRODUCTS";
        Map<String, Object> params = new HashMap<String, Object>();
        return jdbcTemplate.query(SQL, params, new ProductMapper());
    }

    public List<Product> getProductsByCategory(String category) {
        String SQL = "SELECT * FROM PRODUCTS WHERE CATEGORY = :category";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("category", category);
        return jdbcTemplate.query(SQL, params, new ProductMapper());
    }

    public Product read(String id) {
        String SQL = "SELECT * FROM PRODUCTS WHERE ID = :id";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        return jdbcTemplate.queryForObject(SQL, params, new ProductMapper());
    }

    /*
    //@Override
    public void updateStock(String productId, long noOfUnit) {
        String SQL = "UPDATE PRODUCTS SET UNITS_IN_STOCK = :unitsInStock WHERE ID = :id";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("unitsInStock", noOfUnit);
        params.put("id", productId);
        jdbcTemplate.update(SQL, params);
    }*/

    private static final class ProductMapper implements RowMapper<Product> {
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setProductId(rs.getString("ID"));
            product.setName(rs.getString("NAME"));
            product.setDescription(rs.getString("DESCRIPTION"));
            product.setUnitPrice(rs.getBigDecimal("UNIT_PRICE"));
            product.setManufacturer(rs.getString("MANUFACTURER"));
            product.setCategory(rs.getString("CATEGORY"));
            product.setCondition(rs.getString("CONDITION"));
            product.setUnitsInStock(rs.getLong("UNITS_IN_STOCK"));
            product.setUnitsInOrder(rs.getLong("UNITS_IN_ORDER"));
            product.setDiscontinued(rs.getBoolean("DISCONTINUED"));
            return product;
        }
    }
}
