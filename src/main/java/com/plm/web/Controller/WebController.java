package com.plm.web.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController
{
  @GetMapping({"/"})
  public String index()
  {
    return "index";
  }
  
  @GetMapping({"/main"})
  public String main()
  {
    return "pages/index";
  }
  
//  @GetMapping({"/pages/nav"})
//  public String nav()
//  {
//    return "pages/nav.html";
//  }
//  
//  @GetMapping({"/pages/report01"})
//  public String report01()
//  {
//    return "pages/report01.html";
//  }
//  
//  @GetMapping({"/pages/loading"})
//  public String loading()
//  {
//    return "pages/loading.html";
//  }
//  @GetMapping({"/pages/inactivate"})
//  public String inactivate()
//  {
//    return "pages/inactivate.html";
//  }
//  @GetMapping({"/pages/Login"})
//  public String Login()
//  {
//    return "pages/Login.html";
//  }
  
  @GetMapping({"/pages/nav.html"})
  public String nav()
  {
    return "pages/nav";
  }
  
  @GetMapping({"/pages/report01.html"})
  public String report01()
  {
    return "pages/report01";
  }
  
  @GetMapping({"/pages/loading.html"})
  public String loading()
  {
    return "pages/loading";
  }
  @GetMapping({"/pages/inactivate.html"})
  public String inactivate()
  {
    return "pages/inactivate";
  }
  @GetMapping({"/pages/Login.html"})
  public String Login()
  {
    return "pages/Login";
  }
  
}
