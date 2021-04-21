package uk.ac.ed.inf.mpatsis.sstubs.AST;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static org.junit.Assert.*;
import static uk.ac.ed.inf.mpatsis.sstubs.AST.ASTUtils.getLineNumberForASTNode;

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
}