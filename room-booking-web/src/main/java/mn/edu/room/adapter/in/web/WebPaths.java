package mn.edu.room.adapter.in.web;

public final class WebPaths {
    private WebPaths() {
    }

    public static String view(String name) {
        return "/WEB-INF/views/" + name + ".jsp";
    }
}
