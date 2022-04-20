package fr.usmb.m1isc.compilation.tp;

import java.util.HashSet;
import java.util.Set;

public class Node {
    public static int COMPTEUR = 0;
    public NodeType _t;
    public String _v;
    public Node _left, _right;

    public Node(NodeType t, String v) {
        _t = t;
        _v = v;
        _left = null;
        _right = null;

    }

    public Node(NodeType t, String v, Node e1, Node e2) {
        _t = t;
        _v = v;
        _left = e1;
        _right = e2;


    }

    @Override
    public String toString() {
        String str = "";
        if (!(_left == null && _right == null))
            str = "(";

        str = str.concat(_v);

        if (_left != null) {
            str = str.concat(" " + _left.toString() + " ");
        }
        if (_right != null) {
            str = str.concat(" " + _right.toString() + " ");
        }
        if (!(_left == null && _right == null))
            str = str.concat(")");

        return str;
    }

    public Set<String> getLet() {

        Set<String> s = new HashSet<String>();
        if ((this._t) == NodeType.EXPRESSION && (this._v) == "let") {
            s.add(this._left._v);
        }
        Set<String> setLeft = this._left != null ? this._left.getLet() : null;
        Set<String> setRight = this._right != null ? this._right.getLet() : null;
        if (setLeft != null)
            s.addAll(setLeft);
        if (setRight != null)
            s.addAll(setRight);
        return s;
    }

    public String generer() {
        switch (this._t) {
            case SEQUENCE:
                return ( _left==null?"":_left.generer())+(_right==null?"":_right.generer());
            case EXPRESSION:
                return genererExpression();
            case EXPR:
                return genererExpr();
            case VAR:
            case INT:
                return "\t\tmov eax, " + this._v + "\n" +
                        "\t\tpush eax\n";
            case OUTPUT:
                return genererExpr();
            case INPUT:
                return genererExpr();
            case NIL:
                return genererExpr();
            default:
                return genererExpr();
        }

    }

    public String genererExpression() {
        String res = "";
        int tempCpt = 0;
        String expressionGauche;
        String expressionDroite;
        Node temp;
        switch (this._v) {
            case "let":
                String fils_droit = this._right.generer();
                res +=
                        fils_droit +
                                "\t\tmov " + this._left._v +
                                ", eax\n"
                ;
                break;
            case "while":
                tempCpt = COMPTEUR++;
                res += "\tetiq_debut_while_" + tempCpt + ":\n" +
                        this._left.generer() +
                        "\t\tjz etiq_fin_while_" + tempCpt + "\n" +
                        this._right.generer() +
                        "\t\tjmp etiq_debut_while_" + tempCpt + "\n" +
                        "\tetiq_fin_while_" + tempCpt + ":\n";
                break;
            case "if":
                tempCpt = COMPTEUR++;
                res += this._left.generer() +
                        "\t\tjz etiq_if_sinon_" + tempCpt + "\n" +
                        this._right._left.generer() +
                        "\t\tjmp etiq_if_fin_" + tempCpt + "\n" +
                        "\tetiq_if_sinon_" + tempCpt + ":\n" +
                        (this._right._right == null ? "" : this._right._right.generer()) +
                        "\tetiq_if_fin_" + tempCpt + ":\n";
                break;
        }


        return res;
    }

    public String genererExpr() {
        String res = "";
        String expressionGauche;
        String expressionDroite;
        int tempCpt = 0;
        Node temp;
        switch (this._v) {
            case "+":
                expressionGauche = this._left.generer();
                expressionDroite = this._right.generer();
                res += expressionGauche + expressionDroite +
                        "\t\tpop ebx\n" +
                        "\t\tpop eax\n" +
                        "\t\tadd eax, ebx\n" +
                        "\t\tpush eax\n";
                break;
            case "-":
                expressionGauche = this._left.generer();
                expressionDroite = this._right.generer();
                res += expressionGauche + expressionDroite +
                        "\t\tpop ebx\n" +
                        "\t\tpop eax\n" +
                        "\t\tsub eax, ebx\n" +
                        "\t\tpush eax\n";
                break;
            case "*":
                expressionGauche = this._left.generer();
                expressionDroite = this._right.generer();
                res += expressionGauche + expressionDroite +
                        "\t\tpop ebx\n" +
                        "\t\tpop eax\n" +
                        "\t\tmul eax, ebx\n" +
                        "\t\tpush eax\n";
                break;
            case "/":
                expressionGauche = this._left.generer();
                expressionDroite = this._right.generer();
                res += expressionGauche + expressionDroite +
                        "\t\tpop ebx\n" +
                        "\t\tpop eax\n" +
                        "\t\tdiv eax, ebx\n" +
                        "\t\tpush eax\n";
                break;
            case "<":
                tempCpt = COMPTEUR++;
                temp = new Node(NodeType.EXPR, "-", this._left, this._right);
                res += temp.generer() +
                        "\t\tjl etiq_debut_lt_" + tempCpt + "\n" +
                        "\t\tmov eax,0\n" +
                        "\t\tjmp etiq_fin_lt_" + tempCpt + "\n" +
                        "\tetiq_debut_lt_" + tempCpt + ":\n" +
                        "\t\tmov eax,1\n" +
                        "\tetiq_fin_lt_" + tempCpt + ":\n";
                break;
            case "<=":
                tempCpt = COMPTEUR++;
                temp = new Node(NodeType.EXPR, "-", this._left, this._right);
                res += temp.generer() +
                        "\t\tjg etiq_debut_lte_" + tempCpt + "\n" +
                        "\t\tmov eax,0\n" +
                        "\t\tjmp etiq_fin_lte_" + tempCpt + "\n" +
                        "\tetiq_debut_lte_" + tempCpt + ":\n" +
                        "\t\tmov eax,1\n" +
                        "\tetiq_fin_lte_" + tempCpt + ":\n";
                break;
            case ">":
                tempCpt = COMPTEUR++;
                temp = new Node(NodeType.EXPR, "-", this._right, this._left);
                res += temp.generer() +
                        "\t\tjl etiq_debut_gt_" + tempCpt + "\n" +
                        "\t\tmov eax,0\n" +
                        "\t\tjmp etiq_fin_gt_" + tempCpt + "\n" +
                        "\tetiq_debut_gt_" + tempCpt + ":\n" +
                        "\t\tmov eax,1\n" +
                        "\tetiq_fin_gt_" + tempCpt + ":\n";
                break;
            case ">=":
                tempCpt = COMPTEUR++;
                temp = new Node(NodeType.EXPR, "-", this._right, this._left);
                res += temp.generer() +
                        "\t\tjg etiq_debut_gte_" + tempCpt + "\n" +
                        "\t\tmov eax,0\n" +
                        "\t\tjmp etiq_fin_gte_" + tempCpt + "\n" +
                        "\tetiq_debut_gte_" + tempCpt + ":\n" +
                        "\t\tmov eax,1\n" +
                        "\tetiq_fin_gte_" + tempCpt + ":\n";
                break;
            case "and":
                tempCpt = COMPTEUR++;
                expressionGauche = this._left.generer();
                expressionDroite = this._right.generer();
                res +=
                        expressionGauche +
                                "\t\tjz etiq_fin_and_" + tempCpt + "\n" +
                                expressionDroite
                                + "\tetiq_fin_and_" + tempCpt + ":\n";
                break;

        }
        return res;
    }

    public enum NodeType {SEQUENCE, EXPRESSION, EXPR, VAR, INT, OUTPUT, INPUT, NIL}
}
