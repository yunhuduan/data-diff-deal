package cn.com.flaginfo.listener.com.jdbc;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

//@Component exclusions
@WebFilter(urlPatterns={"/*"},initParams={@WebInitParam(name="exclusions",value="/druid/*")})
public class DruidStatFilter extends WebStatFilter {

}
