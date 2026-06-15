package laki.webide.core.html;

import java.util.ArrayList;
import laki.webide.core.CreateBlock;

/**
 * Registry for HTML structural blocks used in the Design Editor.
 */
public class HtmlBlocks {

    public static ArrayList<CreateBlock> getAllHtmlBlocks() {
        ArrayList<CreateBlock> list = new ArrayList<>();
        
        // --- Category: Structure ---
        list.add(CreateBlock.container("div", "Structure", "html_div", "<div>%s</div>"));
        list.add(CreateBlock.container("header", "Structure", "html_header", "<header>%s</header>"));
        list.add(CreateBlock.container("footer", "Structure", "html_footer", "<footer>%s</footer>"));
        list.add(CreateBlock.container("section", "Structure", "html_section", "<section>%s</section>"));
        list.add(CreateBlock.container("nav", "Structure", "html_nav", "<nav>%s</nav>"));
        list.add(CreateBlock.container("main", "Structure", "html_main", "<main>%s</main>"));

        // --- Category: Text Content ---
        list.add(CreateBlock.command("p %s.text", "Text Content", "html_p", "<p>%s</p>"));
        list.add(CreateBlock.command("span %s.text", "Text Content", "html_span", "<span>%s</span>"));
        list.add(CreateBlock.command("h1 %s.text", "Text Content", "html_h1", "<h1>%s</h1>"));
        list.add(CreateBlock.command("h2 %s.text", "Text Content", "html_h2", "<h2>%s</h2>"));
        list.add(CreateBlock.command("h3 %s.text", "Text Content", "html_h3", "<h3>%s</h3>"));

        // --- Category: Lists ---
        list.add(CreateBlock.container("ul", "Lists", "html_ul", "<ul>%s</ul>"));
        list.add(CreateBlock.container("ol", "Lists", "html_ol", "<ol>%s</ol>"));
        list.add(CreateBlock.container("li", "Lists", "html_li", "<li>%s</li>"));

        // --- Category: Forms ---
        list.add(CreateBlock.container("form", "Forms", "html_form", "<form>%s</form>"));
        list.add(CreateBlock.command("input type %m.inputType", "Forms", "html_input", "<input type='%s'>"));
        list.add(CreateBlock.command("button label %s.text", "Forms", "html_button", "<button>%s</button>"));

        // --- Category: Media ---
        list.add(CreateBlock.command("a link to %s.url with text %s.text", "Media", "html_a", "<a href='%s'>%s</a>"));
        list.add(CreateBlock.command("img src %s.url", "Media", "html_img", "<img src='%s'>"));
        
        return list;
    }

    public static ArrayList<CreateBlock> getTestBlocks() {
        return getAllHtmlBlocks();
    }
}
