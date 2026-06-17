package laki.webide.core.html;

import java.util.ArrayList;
import laki.webide.core.CreateBlock;

/**
 * Registry for HTML blocks with unified ID holes.
 */
public class HtmlBlocks {

    public static ArrayList<CreateBlock> getAllHtmlBlocks() {
        ArrayList<CreateBlock> list = new ArrayList<>();
        
        // --- Category: Structure ---
        list.add(CreateBlock.container("div #%s.id", "Structure", "html_div", "<div id='%s' class='%s'>%s</div>"));
        list.add(CreateBlock.container("header #%s.id", "Structure", "html_header", "<header id='%s' class='%s'>%s</header>"));
        list.add(CreateBlock.container("footer #%s.id", "Structure", "html_footer", "<footer id='%s' class='%s'>%s</footer>"));
        list.add(CreateBlock.container("section #%s.id", "Structure", "html_section", "<section id='%s' class='%s'>%s</section>"));
        list.add(CreateBlock.container("nav #%s.id", "Structure", "html_nav", "<nav id='%s' class='%s'>%s</nav>"));
        list.add(CreateBlock.container("main #%s.id", "Structure", "html_main", "<main id='%s' class='%s'>%s</main>"));

        // --- Category: Text Content ---
        // Heading: Hole 0 = Tag, Hole 1 = ID
        list.add(CreateBlock.command("H %m.h_tags #%s.id", "Text Content", "html_h", "<%s id='%s' class='%s'>%s</%s>"));
        
        list.add(CreateBlock.command("p #%s.id", "Text Content", "html_p", "<p id='%s' class='%s'>%s</p>"));
        list.add(CreateBlock.command("span #%s.id", "Text Content", "html_span", "<span id='%s' class='%s'>%s</span>"));
        list.add(CreateBlock.command("label #%s.id", "Text Content", "html_label", "<label id='%s' class='%s'>%s</label>"));
        list.add(CreateBlock.command("br #%s.id", "Text Content", "html_br", "<br id='%s' class='%s'>"));
        list.add(CreateBlock.command("hr #%s.id", "Text Content", "html_hr", "<hr id='%s' class='%s'>"));

        // --- Category: Lists ---
        list.add(CreateBlock.container("ul #%s.id", "Lists", "html_ul", "<ul id='%s' class='%s'>%s</ul>"));
        list.add(CreateBlock.container("ol #%s.id", "Lists", "html_ol", "<ol id='%s' class='%s'>%s</ol>"));
        list.add(CreateBlock.container("li #%s.id", "Lists", "html_li", "<li id='%s' class='%s'>%s</li>"));

        // --- Category: Forms ---
        list.add(CreateBlock.container("form #%s.id", "Forms", "html_form", "<form id='%s' class='%s'>%s</form>"));
        list.add(CreateBlock.command("input #%s.id", "Forms", "html_input", "<input id='%s' class='%s' type='%s'>"));
        list.add(CreateBlock.command("button #%s.id", "Forms", "html_button", "<button id='%s' class='%s'>%s</button>"));

        // --- Category: Media ---
        list.add(CreateBlock.command("link #%s.id", "Media", "html_a", "<a id='%s' class='%s' href='%s'>%s</a>"));
        list.add(CreateBlock.command("img #%s.id", "Media", "html_img", "<img id='%s' class='%s' src='%s'>"));
        
        return list;
    }
}
