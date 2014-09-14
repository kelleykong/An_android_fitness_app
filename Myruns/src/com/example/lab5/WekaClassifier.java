package com.example.lab5;



// Generated with Weka 3.6.11
//
// This code is public domain and comes with no warranty.
//
// Timestamp: Mon May 05 21:38:15 EDT 2014


/*
class WekaClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = WekaClassifier.N5ab6e2e3426(i);
    return p;
  }
  static double N5ab6e2e3426(Object []i) {
    double p = Double.NaN;
    if (i[64] == null) {
      p = 0;
    } else if (((Double) i[64]).doubleValue() <= 0.877651) {
      p = 0;
    } else if (((Double) i[64]).doubleValue() > 0.877651) {
    p = WekaClassifier.N1ce9d39c427(i);
    } 
    return p;
  }
  static double N1ce9d39c427(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 1476.673104) {
    p = WekaClassifier.N3b43b598428(i);
    } else if (((Double) i[0]).doubleValue() > 1476.673104) {
    p = WekaClassifier.N4d1793dc476(i);
    } 
    return p;
  }
  static double N3b43b598428(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 30.45164) {
    p = WekaClassifier.N5a472c8d429(i);
    } else if (((Double) i[5]).doubleValue() > 30.45164) {
    p = WekaClassifier.N1c7cd70c459(i);
    } 
    return p;
  }
  static double N5a472c8d429(Object []i) {
    double p = Double.NaN;
    if (i[6] == null) {
      p = 1;
    } else if (((Double) i[6]).doubleValue() <= 25.578206) {
    p = WekaClassifier.N76be0b48430(i);
    } else if (((Double) i[6]).doubleValue() > 25.578206) {
    p = WekaClassifier.N58c671b0456(i);
    } 
    return p;
  }
  static double N76be0b48430(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 369.357778) {
    p = WekaClassifier.N36d6e0df431(i);
    } else if (((Double) i[0]).doubleValue() > 369.357778) {
    p = WekaClassifier.N2dd0740e437(i);
    } 
    return p;
  }
  static double N36d6e0df431(Object []i) {
    double p = Double.NaN;
    if (i[30] == null) {
      p = 1;
    } else if (((Double) i[30]).doubleValue() <= 2.329845) {
    p = WekaClassifier.N55456c99432(i);
    } else if (((Double) i[30]).doubleValue() > 2.329845) {
      p = 1;
    } 
    return p;
  }
  static double N55456c99432(Object []i) {
    double p = Double.NaN;
    if (i[9] == null) {
      p = 1;
    } else if (((Double) i[9]).doubleValue() <= 4.999593) {
    p = WekaClassifier.N40dcdc8b433(i);
    } else if (((Double) i[9]).doubleValue() > 4.999593) {
    p = WekaClassifier.N64134af1434(i);
    } 
    return p;
  }
  static double N40dcdc8b433(Object []i) {
    double p = Double.NaN;
    if (i[64] == null) {
      p = 2;
    } else if (((Double) i[64]).doubleValue() <= 2.573322) {
      p = 2;
    } else if (((Double) i[64]).doubleValue() > 2.573322) {
      p = 1;
    } 
    return p;
  }
  static double N64134af1434(Object []i) {
    double p = Double.NaN;
    if (i[30] == null) {
      p = 2;
    } else if (((Double) i[30]).doubleValue() <= 1.063072) {
      p = 2;
    } else if (((Double) i[30]).doubleValue() > 1.063072) {
    p = WekaClassifier.N2e9d478b435(i);
    } 
    return p;
  }
  static double N2e9d478b435(Object []i) {
    double p = Double.NaN;
    if (i[9] == null) {
      p = 2;
    } else if (((Double) i[9]).doubleValue() <= 5.484054) {
      p = 2;
    } else if (((Double) i[9]).doubleValue() > 5.484054) {
    p = WekaClassifier.N57c40e95436(i);
    } 
    return p;
  }
  static double N57c40e95436(Object []i) {
    double p = Double.NaN;
    if (i[30] == null) {
      p = 1;
    } else if (((Double) i[30]).doubleValue() <= 2.052803) {
      p = 1;
    } else if (((Double) i[30]).doubleValue() > 2.052803) {
      p = 2;
    } 
    return p;
  }
  static double N2dd0740e437(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 12.903972) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() > 12.903972) {
    p = WekaClassifier.N50735aa1438(i);
    } 
    return p;
  }
  static double N50735aa1438(Object []i) {
    double p = Double.NaN;
    if (i[19] == null) {
      p = 1;
    } else if (((Double) i[19]).doubleValue() <= 5.32413) {
    p = WekaClassifier.N4543294a439(i);
    } else if (((Double) i[19]).doubleValue() > 5.32413) {
      p = 1;
    } 
    return p;
  }
  static double N4543294a439(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 16.834271) {
    p = WekaClassifier.N3c4bdccc440(i);
    } else if (((Double) i[5]).doubleValue() > 16.834271) {
    p = WekaClassifier.N4de77a3e442(i);
    } 
    return p;
  }
  static double N3c4bdccc440(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 1;
    } else if (((Double) i[7]).doubleValue() <= 12.716551) {
      p = 1;
    } else if (((Double) i[7]).doubleValue() > 12.716551) {
    p = WekaClassifier.N188cf401441(i);
    } 
    return p;
  }
  static double N188cf401441(Object []i) {
    double p = Double.NaN;
    if (i[19] == null) {
      p = 2;
    } else if (((Double) i[19]).doubleValue() <= 2.819057) {
      p = 2;
    } else if (((Double) i[19]).doubleValue() > 2.819057) {
      p = 1;
    } 
    return p;
  }
  static double N4de77a3e442(Object []i) {
    double p = Double.NaN;
    if (i[19] == null) {
      p = 2;
    } else if (((Double) i[19]).doubleValue() <= 1.201478) {
    p = WekaClassifier.N180aa467443(i);
    } else if (((Double) i[19]).doubleValue() > 1.201478) {
    p = WekaClassifier.N6c87ac60445(i);
    } 
    return p;
  }
  static double N180aa467443(Object []i) {
    double p = Double.NaN;
    if (i[10] == null) {
      p = 1;
    } else if (((Double) i[10]).doubleValue() <= 4.536986) {
    p = WekaClassifier.N62af7285444(i);
    } else if (((Double) i[10]).doubleValue() > 4.536986) {
      p = 2;
    } 
    return p;
  }
  static double N62af7285444(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 2;
    } else if (((Double) i[7]).doubleValue() <= 1.788457) {
      p = 2;
    } else if (((Double) i[7]).doubleValue() > 1.788457) {
      p = 1;
    } 
    return p;
  }
  static double N6c87ac60445(Object []i) {
    double p = Double.NaN;
    if (i[15] == null) {
      p = 1;
    } else if (((Double) i[15]).doubleValue() <= 4.444011) {
    p = WekaClassifier.N3f460a4a446(i);
    } else if (((Double) i[15]).doubleValue() > 4.444011) {
    p = WekaClassifier.N7711c37e452(i);
    } 
    return p;
  }
  static double N3f460a4a446(Object []i) {
    double p = Double.NaN;
    if (i[8] == null) {
      p = 1;
    } else if (((Double) i[8]).doubleValue() <= 5.277028) {
      p = 1;
    } else if (((Double) i[8]).doubleValue() > 5.277028) {
    p = WekaClassifier.Nf4da0ba447(i);
    } 
    return p;
  }
  static double Nf4da0ba447(Object []i) {
    double p = Double.NaN;
    if (i[18] == null) {
      p = 2;
    } else if (((Double) i[18]).doubleValue() <= 1.443206) {
      p = 2;
    } else if (((Double) i[18]).doubleValue() > 1.443206) {
    p = WekaClassifier.N31731b2f448(i);
    } 
    return p;
  }
  static double N31731b2f448(Object []i) {
    double p = Double.NaN;
    if (i[12] == null) {
      p = 1;
    } else if (((Double) i[12]).doubleValue() <= 3.931839) {
      p = 1;
    } else if (((Double) i[12]).doubleValue() > 3.931839) {
    p = WekaClassifier.N7bfdc405449(i);
    } 
    return p;
  }
  static double N7bfdc405449(Object []i) {
    double p = Double.NaN;
    if (i[18] == null) {
      p = 1;
    } else if (((Double) i[18]).doubleValue() <= 4.325754) {
    p = WekaClassifier.N515063db450(i);
    } else if (((Double) i[18]).doubleValue() > 4.325754) {
      p = 2;
    } 
    return p;
  }
  static double N515063db450(Object []i) {
    double p = Double.NaN;
    if (i[17] == null) {
      p = 1;
    } else if (((Double) i[17]).doubleValue() <= 3.451671) {
    p = WekaClassifier.N74cbe891451(i);
    } else if (((Double) i[17]).doubleValue() > 3.451671) {
      p = 1;
    } 
    return p;
  }
  static double N74cbe891451(Object []i) {
    double p = Double.NaN;
    if (i[29] == null) {
      p = 1;
    } else if (((Double) i[29]).doubleValue() <= 2.599928) {
      p = 1;
    } else if (((Double) i[29]).doubleValue() > 2.599928) {
      p = 2;
    } 
    return p;
  }
  static double N7711c37e452(Object []i) {
    double p = Double.NaN;
    if (i[9] == null) {
      p = 1;
    } else if (((Double) i[9]).doubleValue() <= 10.567602) {
      p = 1;
    } else if (((Double) i[9]).doubleValue() > 10.567602) {
    p = WekaClassifier.N2f39c244453(i);
    } 
    return p;
  }
  static double N2f39c244453(Object []i) {
    double p = Double.NaN;
    if (i[21] == null) {
      p = 1;
    } else if (((Double) i[21]).doubleValue() <= 5.534725) {
    p = WekaClassifier.N79011694454(i);
    } else if (((Double) i[21]).doubleValue() > 5.534725) {
      p = 2;
    } 
    return p;
  }
  static double N79011694454(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 1;
    } else if (((Double) i[7]).doubleValue() <= 15.910049) {
      p = 1;
    } else if (((Double) i[7]).doubleValue() > 15.910049) {
    p = WekaClassifier.N36718c9c455(i);
    } 
    return p;
  }
  static double N36718c9c455(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 2;
    } else if (((Double) i[3]).doubleValue() <= 53.124798) {
      p = 2;
    } else if (((Double) i[3]).doubleValue() > 53.124798) {
      p = 1;
    } 
    return p;
  }
  static double N58c671b0456(Object []i) {
    double p = Double.NaN;
    if (i[22] == null) {
      p = 2;
    } else if (((Double) i[22]).doubleValue() <= 5.640715) {
      p = 2;
    } else if (((Double) i[22]).doubleValue() > 5.640715) {
    p = WekaClassifier.N4c520758457(i);
    } 
    return p;
  }
  static double N4c520758457(Object []i) {
    double p = Double.NaN;
    if (i[8] == null) {
      p = 1;
    } else if (((Double) i[8]).doubleValue() <= 22.298605) {
      p = 1;
    } else if (((Double) i[8]).doubleValue() > 22.298605) {
    p = WekaClassifier.N1d60498d458(i);
    } 
    return p;
  }
  static double N1d60498d458(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 2;
    } else if (((Double) i[1]).doubleValue() <= 215.30341) {
      p = 2;
    } else if (((Double) i[1]).doubleValue() > 215.30341) {
      p = 1;
    } 
    return p;
  }
  static double N1c7cd70c459(Object []i) {
    double p = Double.NaN;
    if (i[12] == null) {
      p = 2;
    } else if (((Double) i[12]).doubleValue() <= 4.193356) {
      p = 2;
    } else if (((Double) i[12]).doubleValue() > 4.193356) {
    p = WekaClassifier.N480a6370460(i);
    } 
    return p;
  }
  static double N480a6370460(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() <= 183.24533) {
    p = WekaClassifier.N22067303461(i);
    } else if (((Double) i[2]).doubleValue() > 183.24533) {
    p = WekaClassifier.N5c393d95473(i);
    } 
    return p;
  }
  static double N22067303461(Object []i) {
    double p = Double.NaN;
    if (i[8] == null) {
      p = 2;
    } else if (((Double) i[8]).doubleValue() <= 6.680813) {
      p = 2;
    } else if (((Double) i[8]).doubleValue() > 6.680813) {
    p = WekaClassifier.N5568db68462(i);
    } 
    return p;
  }
  static double N5568db68462(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 44.563933) {
    p = WekaClassifier.N571cb4a6463(i);
    } else if (((Double) i[5]).doubleValue() > 44.563933) {
    p = WekaClassifier.N207fa3f6467(i);
    } 
    return p;
  }
  static double N571cb4a6463(Object []i) {
    double p = Double.NaN;
    if (i[64] == null) {
      p = 1;
    } else if (((Double) i[64]).doubleValue() <= 27.453196) {
      p = 1;
    } else if (((Double) i[64]).doubleValue() > 27.453196) {
    p = WekaClassifier.N1d982af8464(i);
    } 
    return p;
  }
  static double N1d982af8464(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 2;
    } else if (((Double) i[3]).doubleValue() <= 40.101931) {
      p = 2;
    } else if (((Double) i[3]).doubleValue() > 40.101931) {
    p = WekaClassifier.N712d0ef5465(i);
    } 
    return p;
  }
  static double N712d0ef5465(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 37.912127) {
    p = WekaClassifier.N453130df466(i);
    } else if (((Double) i[4]).doubleValue() > 37.912127) {
      p = 1;
    } 
    return p;
  }
  static double N453130df466(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 2;
    } else if (((Double) i[0]).doubleValue() <= 1068.128988) {
      p = 2;
    } else if (((Double) i[0]).doubleValue() > 1068.128988) {
      p = 1;
    } 
    return p;
  }
  static double N207fa3f6467(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 2;
    } else if (((Double) i[4]).doubleValue() <= 63.138851) {
    p = WekaClassifier.N1be57c25468(i);
    } else if (((Double) i[4]).doubleValue() > 63.138851) {
    p = WekaClassifier.N8763c3c471(i);
    } 
    return p;
  }
  static double N1be57c25468(Object []i) {
    double p = Double.NaN;
    if (i[32] == null) {
      p = 2;
    } else if (((Double) i[32]).doubleValue() <= 6.043352) {
      p = 2;
    } else if (((Double) i[32]).doubleValue() > 6.043352) {
    p = WekaClassifier.N77396f71469(i);
    } 
    return p;
  }
  static double N77396f71469(Object []i) {
    double p = Double.NaN;
    if (i[10] == null) {
      p = 1;
    } else if (((Double) i[10]).doubleValue() <= 20.028599) {
    p = WekaClassifier.N5bc3a0dd470(i);
    } else if (((Double) i[10]).doubleValue() > 20.028599) {
      p = 2;
    } 
    return p;
  }
  static double N5bc3a0dd470(Object []i) {
    double p = Double.NaN;
    if (i[20] == null) {
      p = 1;
    } else if (((Double) i[20]).doubleValue() <= 10.934) {
      p = 1;
    } else if (((Double) i[20]).doubleValue() > 10.934) {
      p = 2;
    } 
    return p;
  }
  static double N8763c3c471(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 99.829524) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() > 99.829524) {
    p = WekaClassifier.Na6c8b7b472(i);
    } 
    return p;
  }
  static double Na6c8b7b472(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 2;
    } else if (((Double) i[0]).doubleValue() <= 1001.913186) {
      p = 2;
    } else if (((Double) i[0]).doubleValue() > 1001.913186) {
      p = 1;
    } 
    return p;
  }
  static double N5c393d95473(Object []i) {
    double p = Double.NaN;
    if (i[64] == null) {
      p = 2;
    } else if (((Double) i[64]).doubleValue() <= 39.439099) {
    p = WekaClassifier.N31fa3080474(i);
    } else if (((Double) i[64]).doubleValue() > 39.439099) {
      p = 1;
    } 
    return p;
  }
  static double N31fa3080474(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 2;
    } else if (((Double) i[5]).doubleValue() <= 38.024851) {
    p = WekaClassifier.N20863d22475(i);
    } else if (((Double) i[5]).doubleValue() > 38.024851) {
      p = 2;
    } 
    return p;
  }
  static double N20863d22475(Object []i) {
    double p = Double.NaN;
    if (i[8] == null) {
      p = 1;
    } else if (((Double) i[8]).doubleValue() <= 14.348141) {
      p = 1;
    } else if (((Double) i[8]).doubleValue() > 14.348141) {
      p = 2;
    } 
    return p;
  }
  static double N4d1793dc476(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 1757.682022) {
    p = WekaClassifier.N46ec780e477(i);
    } else if (((Double) i[0]).doubleValue() > 1757.682022) {
    p = WekaClassifier.N6045e90f489(i);
    } 
    return p;
  }
  static double N46ec780e477(Object []i) {
    double p = Double.NaN;
    if (i[8] == null) {
      p = 1;
    } else if (((Double) i[8]).doubleValue() <= 5.55402) {
      p = 1;
    } else if (((Double) i[8]).doubleValue() > 5.55402) {
    p = WekaClassifier.N4ebe0382478(i);
    } 
    return p;
  }
  static double N4ebe0382478(Object []i) {
    double p = Double.NaN;
    if (i[7] == null) {
      p = 1;
    } else if (((Double) i[7]).doubleValue() <= 23.035205) {
    p = WekaClassifier.N1cd87431479(i);
    } else if (((Double) i[7]).doubleValue() > 23.035205) {
    p = WekaClassifier.N34430d2f487(i);
    } 
    return p;
  }
  static double N1cd87431479(Object []i) {
    double p = Double.NaN;
    if (i[25] == null) {
      p = 2;
    } else if (((Double) i[25]).doubleValue() <= 0.930253) {
      p = 2;
    } else if (((Double) i[25]).doubleValue() > 0.930253) {
    p = WekaClassifier.N46b44bc2480(i);
    } 
    return p;
  }
  static double N46b44bc2480(Object []i) {
    double p = Double.NaN;
    if (i[8] == null) {
      p = 1;
    } else if (((Double) i[8]).doubleValue() <= 23.40072) {
    p = WekaClassifier.N66d9d1d1481(i);
    } else if (((Double) i[8]).doubleValue() > 23.40072) {
      p = 2;
    } 
    return p;
  }
  static double N66d9d1d1481(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 1623.598446) {
    p = WekaClassifier.N665e2517482(i);
    } else if (((Double) i[0]).doubleValue() > 1623.598446) {
    p = WekaClassifier.N31b939d3484(i);
    } 
    return p;
  }
  static double N665e2517482(Object []i) {
    double p = Double.NaN;
    if (i[5] == null) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() <= 39.720299) {
      p = 1;
    } else if (((Double) i[5]).doubleValue() > 39.720299) {
    p = WekaClassifier.N2ed53d82483(i);
    } 
    return p;
  }
  static double N2ed53d82483(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 2;
    } else if (((Double) i[1]).doubleValue() <= 234.150774) {
      p = 2;
    } else if (((Double) i[1]).doubleValue() > 234.150774) {
      p = 1;
    } 
    return p;
  }
  static double N31b939d3484(Object []i) {
    double p = Double.NaN;
    if (i[20] == null) {
      p = 2;
    } else if (((Double) i[20]).doubleValue() <= 6.62102) {
    p = WekaClassifier.N77836525485(i);
    } else if (((Double) i[20]).doubleValue() > 6.62102) {
      p = 1;
    } 
    return p;
  }
  static double N77836525485(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 1;
    } else if (((Double) i[2]).doubleValue() <= 104.788761) {
    p = WekaClassifier.N53659d6f486(i);
    } else if (((Double) i[2]).doubleValue() > 104.788761) {
      p = 2;
    } 
    return p;
  }
  static double N53659d6f486(Object []i) {
    double p = Double.NaN;
    if (i[1] == null) {
      p = 2;
    } else if (((Double) i[1]).doubleValue() <= 141.252505) {
      p = 2;
    } else if (((Double) i[1]).doubleValue() > 141.252505) {
      p = 1;
    } 
    return p;
  }
  static double N34430d2f487(Object []i) {
    double p = Double.NaN;
    if (i[21] == null) {
      p = 2;
    } else if (((Double) i[21]).doubleValue() <= 8.020822) {
      p = 2;
    } else if (((Double) i[21]).doubleValue() > 8.020822) {
    p = WekaClassifier.N1e16a377488(i);
    } 
    return p;
  }
  static double N1e16a377488(Object []i) {
    double p = Double.NaN;
    if (i[3] == null) {
      p = 2;
    } else if (((Double) i[3]).doubleValue() <= 70.648181) {
      p = 2;
    } else if (((Double) i[3]).doubleValue() > 70.648181) {
      p = 1;
    } 
    return p;
  }
  static double N6045e90f489(Object []i) {
    double p = Double.NaN;
    if (i[2] == null) {
      p = 2;
    } else if (((Double) i[2]).doubleValue() <= 102.73891) {
    p = WekaClassifier.Ndc8092a490(i);
    } else if (((Double) i[2]).doubleValue() > 102.73891) {
      p = 2;
    } 
    return p;
  }
  static double Ndc8092a490(Object []i) {
    double p = Double.NaN;
    if (i[4] == null) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() <= 22.96235) {
      p = 1;
    } else if (((Double) i[4]).doubleValue() > 22.96235) {
      p = 2;
    } 
    return p;
  }
}
*/



class WekaClassifier {

  public static double classify(Object[] i)
    throws Exception {

    double p = Double.NaN;
    p = WekaClassifier.N655d77520(i);
    return p;
  }
  static double N655d77520(Object []i) {
    double p = Double.NaN;
    if (i[64] == null) {
      p = 0;
    } else if (((Double) i[64]).doubleValue() <= 0.85372) {
      p = 0;
    } else if (((Double) i[64]).doubleValue() > 0.85372) {
    p = WekaClassifier.N5b44dc7b1(i);
    } 
    return p;
  }
  static double N5b44dc7b1(Object []i) {
    double p = Double.NaN;
    if (i[0] == null) {
      p = 1;
    } else if (((Double) i[0]).doubleValue() <= 315.673995) {
    p = WekaClassifier.N5e73e0d2(i);
    } else if (((Double) i[0]).doubleValue() > 315.673995) {
      p = 2;
    } 
    return p;
  }
  static double N5e73e0d2(Object []i) {
    double p = Double.NaN;
    if (i[13] == null) {
      p = 1;
    } else if (((Double) i[13]).doubleValue() <= 3.521016) {
      p = 1;
    } else if (((Double) i[13]).doubleValue() > 3.521016) {
    p = WekaClassifier.N109aca823(i);
    } 
    return p;
  }
  static double N109aca823(Object []i) {
    double p = Double.NaN;
    if (i[20] == null) {
      p = 2;
    } else if (((Double) i[20]).doubleValue() <= 2.101066) {
      p = 2;
    } else if (((Double) i[20]).doubleValue() > 2.101066) {
      p = 1;
    } 
    return p;
  }
}


