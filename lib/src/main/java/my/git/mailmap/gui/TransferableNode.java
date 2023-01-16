package my.git.mailmap.gui;

import java.awt.datatransfer.Transferable;

import javax.swing.tree.MutableTreeNode;

/**
 * A tag interface combining a MutableTreeNode with a Transferable.
 */
public interface TransferableNode extends MutableTreeNode, Transferable {

}
