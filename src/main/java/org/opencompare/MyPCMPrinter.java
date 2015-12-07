package org.opencompare;

import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.FeatureGroup;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.Value;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.BooleanValue;
import org.opencompare.api.java.value.Conditional;
import org.opencompare.api.java.value.DateValue;
import org.opencompare.api.java.value.Dimension;
import org.opencompare.api.java.value.IntegerValue;
import org.opencompare.api.java.value.Multiple;
import org.opencompare.api.java.value.NotApplicable;
import org.opencompare.api.java.value.NotAvailable;
import org.opencompare.api.java.value.Partial;
import org.opencompare.api.java.value.RealValue;
import org.opencompare.api.java.value.StringValue;
import org.opencompare.api.java.value.Unit;
import org.opencompare.api.java.value.Version;

/**
 * Created by gbecan on 02/02/15.
 */
public class MyPCMPrinter implements PCMVisitor {

    private boolean isBooleanCell;

    /**
     * Print some information contained in a PCM
     * @param pcm: PCM to print
     */
    public void print(PCM pcm) {

        // We start by listing the names of the products
        System.out.println("--- Products ---");
        for (Product product : pcm.getProducts()) {
            System.out.println(product.getName());
        }

        // Then, we use a visitor to print the content of the cells that represent a boolean value
        System.out.println("--- Boolean values ---");
        pcm.accept(this);

    }


    // Methods for the visitor

    @Override
    public void visit(PCM pcm) {
        for (Product product : pcm.getProducts()) {
            product.accept(this);
        }
    }

    @Override
    public void visit(Feature feature) {

    }

    @Override
    public void visit(FeatureGroup featureGroup) {

    }

    @Override
    public void visit(Product product) {
        for (Cell cell : product.getCells()) {
            cell.accept(this);
        }
    }

    @Override
    public void visit(Cell cell) {
        Value interpretation = cell.getInterpretation();

        // Visit the interpretation of the cell to check if it is a boolean
        isBooleanCell = false;
        if (interpretation != null) {
            interpretation.accept(this);
        }

        // Print content of the cell if it is a boolean
        if (isBooleanCell) {
            System.out.println(cell.getContent());
        }
    }

    @Override
    public void visit(BooleanValue booleanValue) {
        isBooleanCell = true;
    }

    @Override
    public void visit(Conditional conditional) {

    }

    @Override
    public void visit(DateValue dateValue) {

    }

    @Override
    public void visit(Dimension dimension) {

    }

    @Override
    public void visit(IntegerValue integerValue) {

    }

    @Override
    public void visit(Multiple multiple) {

    }

    @Override
    public void visit(NotApplicable notApplicable) {

    }

    @Override
    public void visit(NotAvailable notAvailable) {

    }

    @Override
    public void visit(Partial partial) {

    }

    @Override
    public void visit(RealValue realValue) {

    }

    @Override
    public void visit(StringValue stringValue) {

    }

    @Override
    public void visit(Unit unit) {

    }

    @Override
    public void visit(Version version) {

    }
}
