package uk.ac.ed.inf.mpatsis.sstubs.AST;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class ASTUtilsTest {
    private Path workingDir;
    private MethodDeclaration methodToInspect;

    @Before
    public void setUp() throws Exception {
        this.workingDir = Path.of("src/test/resources");
        Path file = this.workingDir.resolve("JavaWriter-CHANGE_UNARY_OPERATOR.java");
        String fileContent = Files.readString(file);

        ASTParser parser = ASTParser.newParser(AST.JLS8);
        HashMap<String, String> options = new HashMap<String, String>(JavaCore.getOptions());
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
        parser.setSource(fileContent.toCharArray());
        parser.setCompilerOptions(options);
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        CompilationUnit codeAST = (CompilationUnit) parser.createAST(null);
        this.methodToInspect = (MethodDeclaration) ((TypeDeclaration) codeAST.types().get(0)).bodyDeclarations().get(12);
    }

    @Test
    public void getLineNumberForASTNode() {
        // go to the line to inspect (line-107)
        IfStatement line = (IfStatement) methodToInspect.getBody().statements().get(10);
        Assert.assertEquals(107, ASTUtils.getLineNumberForASTNode(line));
    }

    @Test
    public void canExtractEnclosingFunctionForChangeUnaryOperator() throws IOException {
        // go to the line to inspect
        IfStatement line = (IfStatement) methodToInspect.getBody().statements().get(10);
        String actualEnclosingFunction = ASTUtils.getEnclosingFunction(line);

        String expectedEnclosingFunction = Files.readString(Path.of("src/test/resources").resolve("expectedEnclosingFunction.txt"));
        Assert.assertEquals(ASTUtils.removeExtraSpacesAndTrim(expectedEnclosingFunction),
                ASTUtils.removeExtraSpacesAndTrim(actualEnclosingFunction));
    }


    @Test
    public void canExtractPrecedingOneLineForChangeUnaryOperator() {
        // go to the line to inspect (line-107)
        IfStatement line = (IfStatement) methodToInspect.getBody().statements().get(10);
        String actualPrecedingLine = ASTUtils.getPrecedingLines(line, 107, 1);

        String expectedPrecedingOneLine = "}";
        Assert.assertEquals(ASTUtils.removeExtraSpacesAndTrim(expectedPrecedingOneLine),
                ASTUtils.removeExtraSpacesAndTrim(actualPrecedingLine));
    }

    @Test
    public void canExtractPrecedingMultipleLinesForChangeUnaryOperator() {
        // go to the line to inspect (line-107)
        IfStatement line = (IfStatement) methodToInspect.getBody().statements().get(10);
        String actualPrecedingLine = ASTUtils.getPrecedingLines(line, 107, 2);

        String expectedPrecedingLines = "} }";
        Assert.assertEquals(ASTUtils.removeExtraSpacesAndTrim(expectedPrecedingLines),
                ASTUtils.removeExtraSpacesAndTrim(actualPrecedingLine));
    }

    @Test
    public void canExtractSucceedingOneLineForChangeUnaryOperator() {
        // go to the line to inspect (line-107)
        IfStatement line = (IfStatement) methodToInspect.getBody().statements().get(10);
        String actualSucceedingLine = ASTUtils.getSucceedingLines(line, 1);

        String expectedSucceedingLine = "appendable.append('\\n');";
        Assert.assertEquals(ASTUtils.removeExtraSpacesAndTrim(expectedSucceedingLine),
                ASTUtils.removeExtraSpacesAndTrim(actualSucceedingLine));
    }

    @Test
    public void canExtractSucceedingMultipleLinesForChangeUnaryOperator() {
        // go to the line to inspect (line-107)
        IfStatement line = (IfStatement) methodToInspect.getBody().statements().get(10);
        String actualSucceedingLines = ASTUtils.getSucceedingLines(line, 2);

        String expectedSucceedingLines = "appendable.append('\\n'); }";
        Assert.assertEquals(ASTUtils.removeExtraSpacesAndTrim(expectedSucceedingLines),
                ASTUtils.removeExtraSpacesAndTrim(actualSucceedingLines));
    }
}