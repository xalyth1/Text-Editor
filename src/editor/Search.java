package editor;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Search extends Thread{

    private String searchString;
    private boolean regex;
    private String text;

    private volatile LinkedList<Indices> result = new LinkedList<>();

    private boolean searchFinished;

    public Search(String searchString, boolean regex, String text) {
        this.searchString = searchString;
        this.regex = regex;
        this.text = text;
    }

    public void run() {
        Pattern pattern = Pattern.compile(regex ? searchString : Pattern.quote(searchString));
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String str = matcher.group();
            result.add(new Indices(str, start, end));
        }
        this.searchFinished = true;
    }


    public synchronized LinkedList<Indices> getResult() {
        return result;
    }

    class Indices {
        String str;
        int start;
        int end;

        public Indices(String str, int start, int end) {
            this.str = str;
            this.start = start;
            this.end = end;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        @Override
        public String toString() {
            return "Indices{" +
                    "str='" + str + '\'' +
                    ", start=" + start +
                    ", end=" + end +
                    '}';
        }
    }




}
