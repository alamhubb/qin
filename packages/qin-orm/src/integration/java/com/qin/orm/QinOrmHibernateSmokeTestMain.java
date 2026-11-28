package com.qin.orm;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceConfiguration;
import jakarta.persistence.Table;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class QinOrmHibernateSmokeTestMain {
    public static void main(String[] args) {
        try (var factory = database();
             var manager = factory.createEntityManager()) {
            seed(manager);

            var db = QinOrm.session(manager);
            var payment = new PaymentQin();
            var customer = new CustomerQin();
            var city = new CityQin();
            var joined = db.select(payment)
                    .join(customer, payment.customerId.eq(customer.id))
                    .leftJoin(city, customer.cityId.eq(city.id))
                    .project(payment.id, city.name)
                    .orderBy(payment.id.asc()).all();
            check(3, joined.size());
            check("Oslo", joined.getFirst()[1]);
            check(null, joined.getLast()[1]);
            expectIllegalArgument(() -> db.select(payment).join(customer, customer.name.eq("Ada")));
            check(1L, db.select(payment).join(customer, payment.customerId.eq(customer.id)
                    .and(customer.name.eq("Lin"))).where(payment.amount.ge(30L)).count());
            var matchedCustomers = db.select(customer).join(payment, customer.id.eq(payment.customerId))
                    .orderBy(customer.id.asc());
            check(2L, matchedCustomers.count());
            var customersPage = matchedCustomers.page(PageRequest.of(0, 10));
            check((long) customersPage.getNumberOfElements(), customersPage.getTotalElements());

            var total = payment.amount.sum();
            check(2L, db.select(payment, payment.status.count()).one());
            check(180L, db.select(payment, total).one());
            check(45.0, db.select(payment, payment.amount.avg()).one());
            check(30L, db.select(payment, payment.amount.min()).one());
            check(70L, db.select(payment, payment.amount.max()).one());
            var grouped = db.select(payment, payment.customerId, total)
                    .groupBy(payment.customerId).having(total.gt(100L));
            check(new Summary(1L, 120L), grouped.as(Summary.class).one());
            check(true, grouped.exists());
            check(1, grouped.slice(PageRequest.of(0, 2)).getNumberOfElements());
            expectUnsupported(() -> grouped.page(PageRequest.of(0, 2)));

            var correlated = db.select(customer, customer.id)
                    .where(customer.id.eq(payment.customerId).and(customer.name.eq("Ada"))).subquery(payment);
            check(2L, db.select(payment).where(correlated.exists()).where(payment.amount.gt(40L)).count());
            expectIllegalArgument(() -> db.select(payment).where(db.select(customer, customer.id)
                    .where(customer.id.eq(payment.customerId)).subquery().exists()).toJpql());
            var customerIds = db.select(customer, customer.id).where(customer.name.eq("Lin")).subquery();
            check(3L, db.select(payment, payment.id).where(payment.customerId.in(customerIds)).one());
            var large = db.select(payment, payment.id).where(payment.amount.ge(50L));
            var first = db.select(payment, payment.id).where(payment.id.eq(1L));
            check(2, large.union(first).all().size());
            check(3, large.unionAll(first).all().size());
            check(1L, large.intersect(first).one());
            check(2L, large.except(first).one());
            check(1L, first.union(large).intersect(first).one());

            check(2L, db.select(payment).countDistinct(payment.status));
            check(3L, db.select(payment, payment.status).distinct()
                    .orderBy(payment.status.asc()).page(PageRequest.of(0, 1)).getTotalElements());
            var distinctAmounts = db.select(payment, payment.amount, payment.status).distinct()
                    .orderBy(payment.amount.asc());
            check(3, distinctAmounts.slice(PageRequest.of(0, 10)).getNumberOfElements());
            expectUnsupported(() -> distinctAmounts.page(PageRequest.of(0, 2)));
            var sorted = db.select(payment).orderBy(Sort.by(Sort.Order.desc("status").ignoreCase().nullsLast()))
                    .orderBy(payment.id.asc());
            check(List.of(1L, 2L, 3L, 4L), sorted.all().stream().map(p -> p.id).toList());
            check(3L, db.select(payment).page(Pageable.unpaged(Sort.by(
                    Sort.Order.asc("status").nullsFirst(), Sort.Order.asc("id"))))
                    .getContent().getFirst().id);
            var scroll = db.select(payment).orderBy(payment.amount.asc(), payment.id.desc());
            var window = scroll.scroll(ScrollPosition.keyset(), 1);
            check(4L, window.getContent().getFirst().id);
            check(3L, scroll.scroll(window.positionAt(0), 1).getContent().getFirst().id);
            var offset = scroll.scroll(ScrollPosition.offset(), 2);
            check(2L, scroll.scroll(offset.positionAt(1), 2).getContent().getFirst().id);
            String paymentTable = QinOrm.nativeIdentifier("qin_payment", Set.of("qin_payment"));
            expectIllegalArgument(() -> QinOrm.nativeIdentifier("other_table", Set.of("qin_payment")));
            expectIllegalArgument(() -> QinOrm.nativeIdentifier("schema.qin_payment", Set.of("schema.qin_payment")));
            check(2L, db.nativeQuery("select count(*) from " + paymentTable + " where customerId = :id")
                    .bind("id", 1L).as(Long.class).one());
            check(new Summary(1L, 120L), db.nativeQuery("select customerId, cast(sum(amount) as bigint) from "
                            + paymentTable + " where customerId = :id group by customerId")
                    .bind("id", 1L).as(Summary.class).one());
            check(List.of(1L, 2L), db.nativeQuery("select row_number() over(order by id) from "
                            + paymentTable + " where customerId = :id")
                    .bind("id", 1L).as(Long.class).all());
        }
        System.out.println("QinOrmHibernateSmokeTestMain OK");
    }

    public static EntityManagerFactory database() {
        return new PersistenceConfiguration("qin-orm-smoke")
                .managedClass(Payment.class)
                .managedClass(Customer.class)
                .managedClass(City.class)
                .property(PersistenceConfiguration.JDBC_URL, "jdbc:h2:mem:qin_orm")
                .property(PersistenceConfiguration.JDBC_USER, "sa")
                .property(PersistenceConfiguration.SCHEMAGEN_DATABASE_ACTION, "drop-and-create")
                .createEntityManagerFactory();
    }

    public static void seed(EntityManager manager) {
        manager.getTransaction().begin();
        manager.persist(new City(1L, "Oslo"));
        manager.persist(new Customer(1L, 1L, "Ada"));
        manager.persist(new Customer(2L, 2L, "Lin"));
        manager.persist(new Payment(1L, 1L, 70L, "Active"));
        manager.persist(new Payment(2L, 1L, 50L, "active"));
        manager.persist(new Payment(3L, 2L, 30L, null));
        manager.persist(new Payment(4L, 9L, 30L, null));
        manager.getTransaction().commit();
        manager.clear();
    }

    private static void check(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("Expected " + expected + ", received " + actual);
        }
    }

    private static void expectIllegalArgument(Runnable action) {
        try {
            action.run();
        } catch (IllegalArgumentException expected) {
            return;
        }
        throw new AssertionError("Expected IllegalArgumentException");
    }

    private static void expectUnsupported(Runnable action) {
        try {
            action.run();
        } catch (UnsupportedOperationException expected) {
            return;
        }
        throw new AssertionError("Expected UnsupportedOperationException");
    }

    public record Summary(long customerId, long total) {
    }

    @Entity
    @Table(name = "qin_payment")
    public static class Payment {
        @Id long id;
        Long customerId;
        Long amount;
        String status;

        protected Payment() {
        }

        Payment(long id, Long customerId, Long amount, String status) {
            this.id = id;
            this.customerId = customerId;
            this.amount = amount;
            this.status = status;
        }
    }

    @Entity
    public static class Customer {
        @Id long id;
        Long cityId;
        String name;

        protected Customer() {
        }

        Customer(long id, Long cityId, String name) {
            this.id = id;
            this.cityId = cityId;
            this.name = name;
        }
    }

    @Entity
    public static class City {
        @Id long id;
        String name;

        protected City() {
        }

        City(long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
