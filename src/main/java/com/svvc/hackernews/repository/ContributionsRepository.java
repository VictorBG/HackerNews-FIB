package com.svvc.hackernews.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.svvc.hackernews.model.ContributionDao;
import com.svvc.hackernews.model.User;


@Repository
public interface ContributionsRepository extends JpaRepository<ContributionDao, Long>
{

    @Query(
        value = "SELECT * FROM contributions WHERE title IS NOT NULL ORDER BY created_at DESC",
        countQuery = "SELECT count(*) FROM contributions where coalesce(link, '') = ''",
        nativeQuery = true)
    List<ContributionDao> findAllByOrderByCreatedAtDesc(Pageable page);

    @Query(
        value = "SELECT * FROM contributions WHERE title IS NOT NULL ORDER BY points desc",
        countQuery = "SELECT count(*) FROM contributions where coalesce(link, '') = ''",
        nativeQuery = true)
    List<ContributionDao> findAllByOrderByPointsDesc(Pageable page);

    @Query(
        value = "SELECT * FROM contributions where coalesce(link, '') = '' AND title IS NOT NULL ORDER BY points DESC",
        countQuery = "SELECT count(*) FROM contributions where coalesce(link, '') = ''",
        nativeQuery = true)
    List<ContributionDao> getAllAsk(Pageable page);

    @Query(
        value = "SELECT * FROM contributions where coalesce(link, '') <> '' ORDER BY points DESC",
        countQuery = "SELECT count(*) FROM contributions where coalesce(link, '') <> ''",
        nativeQuery = true)
    List<ContributionDao> getAllUrl(Pageable page);

    List<ContributionDao> findAllByContributionIdRefOrderByPointsDesc(Long id);

    @Query(
        value = "SELECT * FROM contributions WHERE user_id = ?1 AND contribution_id_ref  <> 0",
        countQuery = "SELECT count(*) FROM contributions c WHERE c.id = ?1 AND c.contributionIdRef  <> 0",
        nativeQuery = true)
    List<ContributionDao> findAllThreadsByUser(String usid, PageRequest pageRequest);

    @Query(
        value = "SELECT * FROM contributions WHERE id = ?1",
        nativeQuery = true)
    ContributionDao findContributionDaoById(Long cId);

    @Query(
        value = "SELECT * FROM contributions WHERE link = ?1",
        nativeQuery = true)
    ContributionDao findContributionDaoByURL(String url);

    @Query(
        value = "SELECT * FROM contributions WHERE user_id = ?1 AND contribution_id_ref  = 0 ORDER BY points DESC",
        nativeQuery = true)
    List<ContributionDao> findAllContributionsById(String usid);

    @Query(
        value = "SELECT * FROM contributions WHERE user_id = ?1 AND contribution_id_ref  = 0 AND points > 0 ORDER BY points DESC",
        nativeQuery = true)
    List<ContributionDao> findAllContributionsVotedById(String usid);

    @Query(
        value = "select count(*) from contributions where contribution_top_parent_id = :contributionId",
        nativeQuery = true)
    int getNumberOfComments(Long contributionId);

    /**
     * Inserts the given input and associates it with the user {@param principal}
     *
     * @param contributionDao       The input to insert into database
     * @param user                  The user who is creating the post
     */
    default ContributionDao insertPost(@NonNull ContributionDao contributionDao, @NonNull final User user)
    {
        contributionDao.setUser(user);
        contributionDao.setCreatedAt(System.currentTimeMillis());
        return save(contributionDao);

    }
}
