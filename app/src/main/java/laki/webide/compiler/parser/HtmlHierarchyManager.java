package laki.webide.compiler.parser;

import com.besome.sketch.beans.ViewBean;
import java.util.Stack;
import java.util.ArrayList;

/**
 * Smart module to handle HTML nesting and self-healing stack logic.
 * Prevents flattened layouts and resolves parent-child relationships across files.
 */
public class HtmlHierarchyManager {
    
    private final Stack<ViewBean> stack = new Stack<>();
    private final ArrayList<ViewBean> allBeans = new ArrayList<>();
    private ViewBean rootMarker;

    public HtmlHierarchyManager() {
        // Initialize with a virtual root
        rootMarker = new ViewBean();
        rootMarker.id = "root";
        stack.push(rootMarker);
    }

    /**
     * Pushes a new view onto the hierarchy. 
     * Automatically resolves the correct parent even if previous tags weren't closed.
     */
    public void pushView(ViewBean bean) {
        if (stack.isEmpty()) {
            stack.push(rootMarker);
        }
        
        ViewBean currentParent = stack.peek();
        bean.parent = currentParent.id;
        bean.parentType = currentParent.type;
        bean.index = getNextIndex(bean.parent);
        
        allBeans.add(bean);
        stack.push(bean);
    }

    public void addBean(ViewBean bean) {
        if (stack.isEmpty()) {
            stack.push(rootMarker);
        }
        
        ViewBean currentParent = stack.peek();
        bean.parent = currentParent.id;
        bean.parentType = currentParent.type;
        bean.index = getNextIndex(bean.parent);
        
        allBeans.add(bean);
    }

    private int getNextIndex(String parentId) {
        int count = 0;
        for (ViewBean b : allBeans) {
            if (parentId.equals(b.parent)) count++;
        }
        return count;
    }

    /**
     * Safely pops a tag from the stack. 
     * If the tag name doesn't match, it "heals" by closing tags until it finds a match.
     */
    public void popView(String tagName) {
        if (stack.size() <= 1) return; // Never pop the rootMarker

        boolean found = false;
        int searchIndex = stack.size() - 1;
        
        while (searchIndex > 0) {
            ViewBean bean = stack.get(searchIndex);
            String tag = bean.parentAttributes.get("html_tag");
            if (tag != null && tag.equalsIgnoreCase(tagName)) {
                found = true;
                break;
            }
            searchIndex--;
        }

        if (found) {
            while (!stack.isEmpty() && stack.size() > 1) {
                ViewBean top = stack.pop();
                String tag = top.parentAttributes.get("html_tag");
                if (tag != null && tag.equalsIgnoreCase(tagName)) {
                    break;
                }
            }
        }
    }

    public void popCurrent() {
        if (stack.size() > 1) {
            stack.pop();
        }
    }

    public ViewBean peek() {
        return stack.isEmpty() ? rootMarker : stack.peek();
    }

    public ArrayList<ViewBean> getResults() {
        return allBeans;
    }
    
    public void reset() {
        stack.clear();
        allBeans.clear();
        stack.push(rootMarker);
    }
}
