package ru.pack.csps.entity;

import javax.persistence.*;

@Entity
@Table(name = "public.deals_borrowers_relations")
public class DealsBorrowersRelations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dbr_id")
    private Long dbrId;

    @Column(name = "dbr_borrower_commission")
    private Double dbrBorrowerCommission;

    @JoinColumn(name = "dbr_deal_id", referencedColumnName = "deal_id")
    @ManyToOne
    private Deals dbrDeal;

    @JoinColumn(name = "dbr_borrower_user_id", referencedColumnName = "user_id")
    @ManyToOne
    private Users dbrBorrowerUser;

    @JoinColumn(name = "dbr_state_id", referencedColumnName = "state_id")
    @ManyToOne
    private States dbrState;

    public DealsBorrowersRelations() {
    }

    public DealsBorrowersRelations(Long dbrId, Deals dbrDeal, Users dbrBorrowerUser, States dbrState) {
        this.dbrId = dbrId;
        this.dbrDeal = dbrDeal;
        this.dbrBorrowerUser = dbrBorrowerUser;
        this.dbrState = dbrState;
    }

    public Long getDbrId() {
        return dbrId;
    }

    public void setDbrId(Long dbrId) {
        this.dbrId = dbrId;
    }

    public Deals getDbrDeal() {
        return dbrDeal;
    }

    public void setDbrDeal(Deals dbrDeal) {
        this.dbrDeal = dbrDeal;
    }

    public Users getDbrBorrowerUser() {
        return dbrBorrowerUser;
    }

    public void setDbrBorrowerUser(Users dbrBorrowerUser) {
        this.dbrBorrowerUser = dbrBorrowerUser;
    }

    public States getDbrState() {
        return dbrState;
    }

    public void setDbrState(States dbrState) {
        this.dbrState = dbrState;
    }

    public Double getDbrBorrowerCommission() {
        return dbrBorrowerCommission;
    }

    public void setDbrBorrowerCommission(Double dbrBorrowerCommission) {
        this.dbrBorrowerCommission = dbrBorrowerCommission;
    }
}
