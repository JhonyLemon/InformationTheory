package pl.polsl.informationtheory.service.compression.algorithm;

import pl.polsl.informationtheory.service.compression.algorithm.entity.HuffmanNode;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Huffman implements Compression {
    private final Map<Byte, String> codes = new HashMap<>();

    @Override
    public byte[] compress(byte[] data) {
        Map<Byte, Integer> frequencyMap = getFrequencyMap(data);
        PriorityQueue<HuffmanNode> priorityQueue = buildPriorityQueue(frequencyMap);

        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();
            HuffmanNode mergedNode = new HuffmanNode((byte) 0, left.getFrequency() + right.getFrequency());
            mergedNode.setLeft(left);
            mergedNode.setRight(right);
            priorityQueue.offer(mergedNode);
        }

        HuffmanNode root = priorityQueue.poll();
        buildCodeMap(root, "");

        StringBuilder compressedData = new StringBuilder();
        for (byte b : data) {
            compressedData.append(codes.get(b));
        }

        return binaryStringToByteArray(compressedData.toString());
    }

    private Map<Byte, Integer> getFrequencyMap(byte[] data) {
        Map<Byte, Integer> frequencyMap = new HashMap<>();
        for (byte b : data) {
            frequencyMap.put(b, frequencyMap.getOrDefault(b, 0) + 1);
        }
        return frequencyMap;
    }

    private PriorityQueue<HuffmanNode> buildPriorityQueue(Map<Byte, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Byte, Integer> entry : frequencyMap.entrySet()) {
            priorityQueue.offer(new HuffmanNode(entry.getKey(), entry.getValue()));
        }
        return priorityQueue;
    }

    private void buildCodeMap(HuffmanNode node, String code) {
        if (node != null) {
            if (node.getLeft() == null && node.getRight() == null) {
                codes.put(node.getData(), code);
            }
            buildCodeMap(node.getLeft(), code + "0");
            buildCodeMap(node.getRight(), code + "1");
        }
    }

    private byte[] binaryStringToByteArray(String binaryString) {
        int length = (int) Math.ceil(binaryString.length() / 8.0);
        byte[] result = new byte[length];
        int index = 0;
        for (int i = 0; i < binaryString.length(); i += 8) {
            String chunk = binaryString.substring(i, Math.min(i + 8, binaryString.length()));
            result[index++] = (byte) Integer.parseInt(chunk, 2);
        }
        return result;
    }
}
