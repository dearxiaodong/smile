package springboot.modal.vo;

public class AttachVo {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_attach.id
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_attach.fname
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    private String fname;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_attach.ftype
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    private String ftype;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_attach.fkey
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    private String fkey;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_attach.author_id
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    private Integer authorId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_attach.created
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    private Integer created;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_attach.id
     *
     * @return the value of t_attach.id
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_attach.id
     *
     * @param id the value for t_attach.id
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_attach.fname
     *
     * @return the value of t_attach.fname
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    public String getFname() {
        return fname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_attach.fname
     *
     * @param fname the value for t_attach.fname
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    public void setFname(String fname) {
        this.fname = fname == null ? null : fname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_attach.ftype
     *
     * @return the value of t_attach.ftype
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    public String getFtype() {
        return ftype;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_attach.ftype
     *
     * @param ftype the value for t_attach.ftype
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    public void setFtype(String ftype) {
        this.ftype = ftype == null ? null : ftype.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_attach.fkey
     *
     * @return the value of t_attach.fkey
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    public String getFkey() {
        return fkey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_attach.fkey
     *
     * @param fkey the value for t_attach.fkey
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    public void setFkey(String fkey) {
        this.fkey = fkey == null ? null : fkey.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_attach.author_id
     *
     * @return the value of t_attach.author_id
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    public Integer getAuthorId() {
        return authorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_attach.author_id
     *
     * @param authorId the value for t_attach.author_id
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_attach.created
     *
     * @return the value of t_attach.created
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    public Integer getCreated() {
        return created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_attach.created
     *
     * @param created the value for t_attach.created
     *
     * @mbg.generated Thu Nov 08 10:42:29 CST 2018
     */
    public void setCreated(Integer created) {
        this.created = created;
    }
}