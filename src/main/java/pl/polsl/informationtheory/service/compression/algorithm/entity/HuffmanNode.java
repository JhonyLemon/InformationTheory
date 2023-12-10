package pl.polsl.informationtheory.service.compression.algorithm.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HuffmanNode implements Comparable<HuffmanNode> {
    private byte data;
    private int frequency;
    private HuffmanNode left, right;

    public HuffmanNode(byte data, int frequency) {
        this.data = data;
        this.frequency = frequency;
    }

    @Override
    public int compareTo(HuffmanNode node) {
        return this.frequency - node.frequency;
    }
}
