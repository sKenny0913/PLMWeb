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
  
  @GetMapping({"/pages/nav"})
  public String nav()
  {
    return "pages/nav.html";
  }
  
  @GetMapping({"/pages/report01"})
  public String report01()
  {
    return "pages/report01.html";
  }
  
  @GetMapping({"/pages/loading"})
  public String loading()
  {
    return "pages/loading.html";
  }
  @GetMapping({"/pages/inactivate"})
  public String inactivate()
  {
    return "pages/inactivate.html";
  }
  @GetMapping({"/pages/Login"})
  public String Login()
  {
    return "pages/Login.html";
  }
}
