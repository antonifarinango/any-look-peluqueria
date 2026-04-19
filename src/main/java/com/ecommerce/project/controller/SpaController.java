package com.ecommerce.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    // Catch-all para redireccionar rutas del frontend al index.html
    // Evita rutas que tengan puntos (archivos estáticos como .js, .css, .png)
    // Permite rutas multinivel como /admin/products o /user/cart
    @GetMapping(value = "/{path:[^\\.]*}")
    public String forward() {
        return "forward:/index.html";
    }

    @GetMapping(value = "/**/{path:[^\\.]*}")
    public String forwardSubPaths() {
        return "forward:/index.html";
    }
}
