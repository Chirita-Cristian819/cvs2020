package com.javaonline;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;

public class WicketTestApplication extends WebApplication
{
	
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return WicketTest.class;
	}

	@Override
	public void init()
	{
		super.init();

	}
}