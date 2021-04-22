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

    public static String getEnclosingFunction(final ASTNode astNode) {
        /*
            reference: https://stackoverflow.com/questions/14449262/how-to-get-the-enclosing-method-node-with-jdt
            ASTNode parentNode = ASTNodes.getParent(methodInvocationNode, ASTNode.METHOD_DECLARATION);
        */
        if (astNode.getNodeType() == ASTNode.METHOD_DECLARATION) {
            return astNode.toString();
        }

        ASTNode enclosingFunctionAstNode = ASTNodes.getParent(astNode, ASTNode.METHOD_DECLARATION);
        if (enclosingFunctionAstNode == null) {
            if (ASTNodes.getParent(astNode, ASTNode.FIELD_DECLARATION) != null) {
                //example: private static final Pattern TYPE_PATTERN=Pattern.compile("(?:[\\w$]+\\.)*([\\w$]+)");
                enclosingFunctionAstNode = ASTNodes.getParent(astNode, ASTNode.COMPILATION_UNIT);
            }

            if (astNode.getNodeType() == ASTNode.TYPE_DECLARATION) {
                enclosingFunctionAstNode = ASTNodes.getParent(astNode, ASTNode.COMPILATION_UNIT);
            }
        }

        String enclosingFunction = enclosingFunctionAstNode == null ? "" : enclosingFunctionAstNode.toString();
        if (enclosingFunction == "") {
            System.err.println("Error: context missing: EnclosingFunction");
        }

        return enclosingFunction;
    }

    public static String getPrecedingLines(ASTNode astNode, int lineNo, int howManyPrecedingLines,
                                           String fileContent) {
        // String fileContent = astNode.getRoot().toString();
        int lineNumber = ((CompilationUnit) astNode.getRoot())
                .getLineNumber(astNode.getStartPosition());

        String contextLines = fileContent.lines()
                .skip(lineNo - howManyPrecedingLines - 1)
                .limit(howManyPrecedingLines)
                .collect(Collectors.joining(" "));

        if (contextLines == "") {
            System.err.println("Error: context missing: getPrecedingLines");
        }

        return contextLines;
    }

    public static String getSucceedingLines(ASTNode astNode, int succeedingLines, String fileContent) {
        // The following line gives normalized fileContext like after removing spaces etc
        // String fileContent = astNode.getRoot().toString();
        //fileContent = astNode.getRoot().getAST().getSourceCode();
        //String fileContent = new String(astNode.getRoot().getAST()..scanner.source)

        int lineNumber = ((CompilationUnit) astNode.getRoot())
                .getLineNumber(astNode.getStartPosition());

        String contextLines = fileContent.lines()
                .skip(lineNumber)
                .limit(succeedingLines)
                .collect(Collectors.joining(" "));

        if (contextLines == "") {
            System.err.println("Error: context missing: getSucceedingLines");
        }
        return contextLines;
    }

    public static String removeExtraSpacesAndTrim(String input) {
        return input.trim().replaceAll("\\s+", " ");
    }
}
