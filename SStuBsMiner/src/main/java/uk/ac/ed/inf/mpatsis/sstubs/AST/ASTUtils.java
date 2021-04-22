package uk.ac.ed.inf.mpatsis.sstubs.AST;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.corext.dom.ASTNodes;

import java.util.stream.Collectors;

public class ASTUtils {

    public static int getLineNumberForASTNode(ASTNode astNode) {
        int lineNum = ((CompilationUnit) astNode.getRoot()).getLineNumber(astNode.getStartPosition());
        return lineNum;
    }

    public static String getEnclosingFunction(ASTNode astNode) {
        /*
            reference: https://stackoverflow.com/questions/14449262/how-to-get-the-enclosing-method-node-with-jdt
            ASTNode parentNode = ASTNodes.getParent(methodInvocationNode, ASTNode.METHOD_DECLARATION);
        */
        ASTNode enclosingFunctionAstNode = ASTNodes.getParent(astNode, ASTNode.METHOD_DECLARATION);
        String enclosingFunction = enclosingFunctionAstNode == null ? "" : enclosingFunctionAstNode.toString();
        return enclosingFunction;
    }

    public static String getPrecedingLines(ASTNode astNode, int lineNo, int howManyPrecedingLines) {
        String fileContent = astNode.getRoot().toString();
        int lineNumber = ((CompilationUnit) astNode.getRoot())
                .getLineNumber(astNode.getStartPosition());

        String contextLines = fileContent.lines()
                .skip(lineNo - howManyPrecedingLines - 1)
                .limit(howManyPrecedingLines)
                .collect(Collectors.joining(" "));

        return contextLines;
    }

    public static String getSucceedingLines(ASTNode astNode, int succeedingLines) {
        String fileContent = astNode.getRoot().toString();
        int lineNumber = ((CompilationUnit) astNode.getRoot())
                .getLineNumber(astNode.getStartPosition());

        String contextLines = fileContent.lines()
                .skip(lineNumber)
                .limit(succeedingLines)
                .collect(Collectors.joining(" "));

        return contextLines;
    }

    public static String removeExtraSpacesAndTrim(String input) {
        return input.trim().replaceAll("\\s+", " ");
    }
}
