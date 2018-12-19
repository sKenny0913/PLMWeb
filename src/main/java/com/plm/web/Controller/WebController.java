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
  
  //--------------.load(plmweb.js, login.js)---------------
  @GetMapping({"/pages/nav.html"})
  public String nav()
  {
    return "pages/nav";
  }
  @GetMapping({"/pages/loading.html"})
  public String loading()
  {
	  return "pages/loading";
  }
  @GetMapping({"/pages/confirm.html"})
  public String confirm()
  {
	  return "pages/confirm";
  }
  @GetMapping({"/pages/Login.html"})
  public String Login()
  {
    return "pages/Login";
  }
  //-------------href(nav.html)----------------
  @GetMapping({"/pages/report01.html"})
  public String report01()
  {
    return "pages/report01";
  }
  
  @GetMapping({"/pages/inactivate.html"})
  public String inactivate()
  {
    return "pages/inactivate";
  }

  @GetMapping({"/pages/ListMaintain.html"})
  public String ListMaintain()
  {
    return "pages/ListMaintain";
  }
  
}
