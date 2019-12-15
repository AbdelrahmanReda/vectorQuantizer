package sample;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class Qunatizer {
    
    String decompressedImage = "E:\\vectorQuantizer\\imagesSamples\\sam\\checkker.jpg" ;
    
    public  int N;
    public  int intr ;
    public  String path ;
    public  ArrayList<Integer> loockUpTable =new ArrayList();
    public  boolean  loock=false;

    public  void printSamlpe(ArrayList<ArrayList<Integer>> oneSample , int sampleName) throws InterruptedException {
        ArrayList<ArrayList<Integer>> Matrices = oneSample;
        int vectorSize = N;
        for (int ii = 0; ii <Matrices.size() ; ii++) {
            BufferedImage sample2 = new BufferedImage(vectorSize,vectorSize,BufferedImage.TYPE_INT_RGB);
            for (int j = 0; j <Matrices.get(ii).size() ; j+=vectorSize) {
                for (int k = j; k <j+vectorSize ; k++) {
                    int temp=Matrices.get(ii).get(k);
                    sample2.setRGB(j%vectorSize+j/vectorSize,k%vectorSize,
                            temp + (temp<<8)+(temp<<16));
                }
            }
        }
    }
    public  ArrayList<ArrayList<Integer>>getSamples(int sampleSize,String pathName) throws InterruptedException {
        File file = new File (pathName);
        BufferedImage amg = null;
        try {
            amg = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int vectorSize = sampleSize; int height = amg.getHeight() ; int width = amg.getWidth() ;
        int counter=0;
        ArrayList<ArrayList<Integer>> Matrices = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i <height ; i=i+vectorSize) {
            ArrayList<ArrayList<Integer>> reader = new ArrayList<>();
            for (int j = 0; j <width ; j=j+vectorSize) {
                ArrayList <Integer> sampleMatrix = new ArrayList<Integer>();
                for (int k = i; k < i+vectorSize ; k++) {

                    for (int l = j; l <j+vectorSize ; l++) {

                        sampleMatrix.add(((amg.getRGB(k,l)&0xffffff)>>16));
                    }
                }
                reader.add(sampleMatrix);
                counter++;
                Matrices.add(sampleMatrix);
            }
        }
        return  Matrices;
    }

    public  ArrayList<ArrayList<Integer>> calculate (ArrayList<ArrayList<Integer>> unlocatedVectors,ArrayList<ArrayList<Integer>> Matrix)
    {
        ArrayList<simpleTree> vector=new ArrayList();
        for (int i = 0; i < Matrix.size() ; i++) {
            simpleTree st =new simpleTree();
            vector.add(st);
        }
        ArrayList <ArrayList<Integer>> differences = new ArrayList<>();
        for (int i = 0; i <Matrix.size() ; i++) { ArrayList <Integer> filler = new ArrayList<>();differences.add(filler);}
        ArrayList<Integer> maximi = new ArrayList<>();
        for (int i = 0; i <unlocatedVectors.size() ; i++) {
            for (int j = 0; j <unlocatedVectors.get(i).size() ; j++) {
                for (int k = 0; k <Matrix.size() ; k++) {
                    int difference = (int)Math.pow(unlocatedVectors.get(i).get(j)-Matrix.get(k).get(j),2);
                    differences.get(k).add(difference);
                }
            }
            for (int j = 0; j <differences.size() ; j++) {
                int summation = 0;
                for (int k = 0; k <differences.get(j).size() ; k++) {

                    summation=summation+differences.get(j).get(k);
                }
                maximi.add(summation);
            }
            if(loock)

            {
                loockUpTable.add(maximi.indexOf(Collections.min(maximi)));
            }
            ArrayList <Integer> temp = new ArrayList<>();
            for (int j = 0; j < unlocatedVectors.get(i).size() ; j++) {

                temp.add(unlocatedVectors.get(i).get(j));
            }
            vector.get(maximi.indexOf(Collections.min(maximi))).mat.add(temp);
            differences.clear();
            maximi.clear();
            for (int ii = 0; ii <Matrix.size() ; ii++) { ArrayList <Integer> filler = new ArrayList<>();differences.add(filler);}
        }
        for (int i = 0; i < vector.size(); i++) {
            Matrix.set(i,getSamplesAverage(vector.get(i).mat));
        }
        return Matrix;
    }

    public  ArrayList<ArrayList<Integer>> slpit (ArrayList<Integer> _allSamples){
        ArrayList<ArrayList<Integer>> splittedSamples = new ArrayList<ArrayList<Integer>>() ;
        ArrayList<Integer> sampleLeft = new ArrayList<>();
        ArrayList<Integer> sampleRight = new ArrayList<>();
        for (int i = 0; i < _allSamples.size(); i++) {

            sampleLeft.add(_allSamples.get(i)-1);
            sampleRight.add(_allSamples.get(i)+1);


        }
        splittedSamples.add(sampleLeft);
        splittedSamples.add(sampleRight);
        return splittedSamples;
    }

    public  ArrayList<Integer> getSamplesAverage(ArrayList<ArrayList<Integer>> _allSamples){
        ArrayList <Integer> AverageResult= new ArrayList<>(Collections.nCopies(N * N, 0));
        for (int i = 0; i <_allSamples.size() ; i++) {
            for (int j = 0; j <_allSamples.get(i).size() ; j++) {
                AverageResult.set(j,AverageResult.get(j)+_allSamples.get(i).get(j));
            }
        }
        for (int i = 0; i <AverageResult.size() ; i++) {
            if(_allSamples.size()!=0)
                AverageResult.set(i,AverageResult.get(i)/_allSamples.size());
        }
        return  AverageResult;
    }

    public  String CompressedImage (){
        
        return decompressedImage ;
    }
    public  ArrayList<ArrayList<Integer>> slpitAnother (ArrayList<ArrayList<Integer>> matrix)
    {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        for (int i = 0; i < matrix.size() ; i++) {
            ArrayList<Integer> left = new ArrayList<>();
            ArrayList<Integer> right = new ArrayList<>();
            for (int j = 0; j <matrix.get(i).size()  ; j++) {
                left.add(matrix.get(i).get(j)-1);
                right.add(matrix.get(i).get(j)+1);

            }
            result.add(right);
            result.add(left);

        }
        return result;
    }
    Qunatizer(int smapleSizeIn , int codeBookSizeIN,String amgPath) throws IOException, InterruptedException {

        path=amgPath;
        N=smapleSizeIn;
        intr=codeBookSizeIN;
        ArrayList<ArrayList<Integer>> Matrices = getSamples(N, path);
        int vectorSize = N;
        for (int ii = 0; ii < Matrices.size(); ii++) {
            BufferedImage sample2 = new BufferedImage(vectorSize, vectorSize, BufferedImage.TYPE_INT_RGB);
            for (int j = 0; j < Matrices.get(ii).size(); j += vectorSize) {
                for (int k = j; k < j + vectorSize; k++) {
                    int temp = Matrices.get(ii).get(k);
                    sample2.setRGB(j % vectorSize + j / vectorSize, k % vectorSize, temp + (temp << 8) + (temp << 8));
                }
            }
        }
        ArrayList<Integer> average = getSamplesAverage(getSamples(N, path));
        ArrayList<ArrayList<Integer>> _2Splits = slpit(average);

        while (!(_2Splits.size() ==intr)) {

            _2Splits = calculate(Matrices, _2Splits);
            _2Splits = slpitAnother(_2Splits);

        }

        loock = true;
        calculate(Matrices, _2Splits);
        ArrayList<Integer> result = new ArrayList<>();
        for (int j = 0; j < loockUpTable.size(); j++) {
            ArrayList<Integer> filler = new ArrayList<>();
            filler = _2Splits.get(loockUpTable.get(j));
            for (int k = 0; k < filler.size(); k++) {
                result.add(filler.get(k));
            }
        }
        Matrices = _2Splits;
        result.add(1);
        //---------------
        result.add(1);
        result.add(1);
        result.add(1);


        //-------------
        int imgSize = 256;
        int smapleSize = N;
        int f = 0;
        ArrayList<BufferedImage> rgbImages = new ArrayList<>();
        for (int p = 3; p < 6; p++) {

            System.out.println("shifting is" + (int) Math.pow(2, p));

            BufferedImage sample = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
            int counter = 0;
            for (int i = 0; i < imgSize; i += smapleSize) {
                for (int j = 0; j < imgSize / smapleSize; j++) {
                    for (int k = i; k < i + smapleSize; k++) {
                        for (int l = f; l < f + smapleSize; l++) {
                            counter++;
                            sample.setRGB(k, l, result.get(counter) << (int) Math.pow(2, p));
                        }
                    }
                    f += smapleSize;
                }
                f = 0;
            }
            rgbImages.add(sample);
        }

        BufferedImage sample = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                sample.setRGB(i, j, rgbImages.get(0).getRGB(i, j) + rgbImages.get(1).getRGB(i, j) + rgbImages.get(2).getRGB(i, j));
            }
        }
        try {
            String path = "E:\\vectorQuantizer\\imagesSamples\\sam\\";
            File file2 = new File(path + "checkker" + ".jpg");
            ImageIO.write(sample, "jpg", file2);
        } catch (IOException e) {
            System.out.println("filled");
        }

        for (int ii = 0; ii < Matrices.size(); ii++) {
            BufferedImage sample2 = new BufferedImage(vectorSize, vectorSize, BufferedImage.TYPE_INT_RGB);
            for (int j = 0; j < Matrices.get(ii).size(); j += vectorSize) {
                for (int k = j; k < j + vectorSize; k++) {
                    int temp = Matrices.get(ii).get(k);
                    sample2.setRGB(j % vectorSize + j / vectorSize, k % vectorSize, temp + (temp << 8) + (temp << 16));
                }
            }

        }
    }



}
