package me.fengyj.springdemo.utils.exceptions;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public record ExceptionDetail(String exceptionType, String message, List<StackDetail> stackDetails, ExceptionDetail innerDetail) {

    @Override
    public String toString() {
        var stackDetail = stackDetails == null || stackDetails.isEmpty() ? null : stackDetails.get(0);
        return String.format(
                "[%s] %s - %s",
                exceptionType,
                message,
                stackDetail == null ? "" : stackDetail.toString());
    }

    public static ExceptionDetail create(Exception ex) {

        return create(ex, 5, 5, new String[]{"me.fengyj"});
    }

    public static ExceptionDetail create(Exception ex, int maxInnerLevel, int maxStackNumbers, String[] stackPackageFilters) {

        Deque<Exception> stack = new LinkedList<>();
        stack.push(ex);
        int level = 1;
        var cause = ex.getCause();
        while (cause instanceof Exception e && level < maxInnerLevel) {
            level++;
            stack.push(e);
            cause = cause.getCause();
        }

        ExceptionDetail detail = null;
        while (!stack.isEmpty()) {
            var e = stack.pop();

            var stackDetails = new LinkedList<StackDetail>();
            int sl = 0;
            for (StackTraceElement st : e.getStackTrace()) {
                boolean matched = true;
                if (stackPackageFilters != null) {
                    matched = false;
                    for (var f : stackPackageFilters) {
                        if (st.getClassName().startsWith(f)) {
                            matched = true;
                            break;
                        }
                    }
                }
                if (matched) {
                    stackDetails.add(new StackDetail(
                            st.getClassName().replaceAll("(\\w)\\w*\\.", "$1."),
                            st.getMethodName(),
                            st.getLineNumber()));
                    sl++;
                }
                if (sl >= maxStackNumbers) break;
            }
            detail = new ExceptionDetail(e.getClass().getSimpleName(), e.getMessage(), stackDetails, detail);
        }

        return detail;
    }

    public record StackDetail(String className, String methodName, int lineNumber) {

        @Override
        public String toString() {

            return String.format("[%s] %s:%d", className, methodName, lineNumber);
        }
    }

}
