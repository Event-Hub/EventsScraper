package com.event.scraper.scraperengine.ebilet;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HTMLUnitExperiments {


    @BeforeAll
    static void beforeAll() {
        System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "error");
    }

    @Test
    public void homePage() throws Exception {
        try (final WebClient webClient = new WebClient()) {
            final HtmlPage page = webClient.getPage("https://htmlunit.sourceforge.io/");
            assertEquals("HtmlUnit – Welcome to HtmlUnit", page.getTitleText());

            final String pageAsXml = page.asXml();
            assertTrue(pageAsXml.contains("<body class=\"topBarDisabled\">"));

            final String pageAsText = page.asNormalizedText();
            assertTrue(pageAsText.contains("Support for the HTTP and HTTPS protocols"));
        }
    }

    @Test
    public void homePage_Firefox() throws Exception {
        try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX)) {
            final HtmlPage page = webClient.getPage("https://htmlunit.sourceforge.io/");
            assertEquals("HtmlUnit – Welcome to HtmlUnit", page.getTitleText());
        }
    }

    @Test
    public void homePageEbilet() throws Exception {
        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {

            final HtmlPage page = webClient.getPage("https://ebilet.pl/muzyka/rock");

            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setUseInsecureSSL(true);


            String previousLast = "";
            String last = "empty";
            do {
                previousLast = new String(last);
                List<DomElement> domElements = getUniqueEventsAchors(webClient, page);
                System.out.println(domElements.size());
                last = domElements.get(domElements.size() - 1).getAttributeDirect("title");
                System.out.println(previousLast);

            } while (!last.equals(previousLast));





        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private List<DomElement> getUniqueEventsAchors(WebClient webClient, HtmlPage page) {
        page.executeJavaScript("window.scrollTo(0, document.body.scrollHeight + 2000);");
        webClient.waitForBackgroundJavaScript(700);

        HtmlPage enclosedPage = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();


        List<DomElement> domElements = enclosedPage.getElementsByTagName("a").stream().filter(domElement -> {
                    return domElement.getAttribute("class").equals("cube");
                }).peek(domElement -> System.out.println(domElement.getAttributeDirect("title")))
                .toList();
        System.out.println(domElements.size());
        return domElements;
    }


    public Document getDocument(String pageContent) {
        return Jsoup.parse(pageContent);

    }


    @Test
    public void homePageEbilet2() throws Exception {
        try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {

            final HtmlPage page = webClient.getPage("https://www.ebilet.pl/umbraco/Surface/SubcategorySurface/GetSubcategoryEventsPage?lang=pl&currentPage=1&subcategoryId=1063");

            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setTimeout(15000);

            page.executeJavaScript("window.scrollBy(0,100000)");
            HtmlPage enclosedPage = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();


            long count = enclosedPage.getElementsByTagName("a").stream().filter(domElement -> {
                        return domElement.getAttribute("class").equals("cube");
                    }).peek(domElement -> System.out.println(domElement.getAttributeDirect("title")))
                    .count();
            System.out.println(count);


        }
    }
}

//https://www.ebilet.pl/umbraco/Surface/SubcategorySurface/GetSubcategoryEventsPage?lang=pl&currentPage=1&subcategoryId=1063