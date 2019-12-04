package ca.jrvs.apps.practice;

public class RegexExcImp implements RegexExc{
   /**
    * return true if filename extension is jpg or jpeg (case insensitive)
    * @param filename
    * @return
    */
    public boolean matchJpeg(String filename){
      return true;
    }

    public boolean matchIp(String ip){
      return true;
    }

    public boolean isEmptyLine(String line){
      return true;
    }

}
