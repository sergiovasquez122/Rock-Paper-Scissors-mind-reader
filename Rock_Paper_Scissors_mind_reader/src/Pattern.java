/**
 * Pattern class - Representation of a single pattern
 * @author Sergio Vasquez
 */
public class Pattern {
    /** The pattern */
    private String pattern;

    /**
     * Constructor - Constructs a Pattern object with pattern p
     * @param p the pattern to be assigned to the Pattern object
     */
    public Pattern(String p){
        this.pattern = p;
    }

    /**
     * Retrieve the pattern
     * @return the pattern
     */
    public String getPattern(){
        return pattern;
    }

    /**
     * Retrieve the hashcode of the pattern
     * @return the hashcode of the pattern
     */
   @Override
   public int hashCode()
   {
        return pattern.hashCode();
   }

    /**
     * Check if an object is equal to the Pattern object
     * @param o the object to be compared to
     * @return true if the objects are equal false otherwise
     */
   @Override
   public boolean equals(Object o){
       if( o == this ) {
           return true;
       }
       if( o == null ) {
           return false;
       }
       if( o.getClass() != getClass() ){
           return false;
       }
       Pattern p = (Pattern) o;
       // Do a string comparison
       return pattern.equals( p.getPattern() );
   }
}
