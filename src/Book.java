public class Book {
    private String bookTitle;
    private String bookNumber;
    private int numCopies;
    private boolean referenceOnly;
    private String publisher;
    private boolean availabilityStatus = true; //usefull for the loan function and return function.
    private boolean reserveStatus = false; //useful for the reserving a book function.

    public void setAvailabilityStatus(boolean availability)
    {
        this.availabilityStatus = availability;
    }
    public void setReserveStatus(boolean reserve)
    {
        this.reserveStatus = reserve;
    }
    public void setBookTitle(String bookTitle)
    {
        this.bookTitle = bookTitle;
    }
    public void setNumCopies(int numCopies)
    {
        this.numCopies = numCopies;
    }
    public void setReferenceOnly(boolean reference)
    {
        this.referenceOnly = reference;
    }
    public void setPublisher(String pub)
    {
        this.publisher = pub;
    }
    public void setBookNumber(String bNumber)
    {
        this.bookNumber = bNumber;
    }
    //have to add a setter for the book copy

    public String getBookTitle()
    {
        return bookTitle;
    }
    public int getNumCopies()
    {
        return numCopies;
    }
    public boolean getReferenceOnly()
    {
        return referenceOnly;
    }
    public String getPublisher()
    {
        return publisher;
    }
    public String getBookNumber()
    {
        return bookNumber;
    }
    public boolean getAvailabilityStatus()
    {
        return availabilityStatus;
    }
    public boolean getReserveStatus()
    {
        return reserveStatus;
    }
}
