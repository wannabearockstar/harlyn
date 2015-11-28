package com.harlyn.domain.problems.handlers;

import com.harlyn.config.ProblemConfig;
import com.harlyn.domain.User;
import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.SubmitData;

/**
 * Created by wannabe on 20.11.15.
 */

/**
 * This interface provide handling of problem. Problems know their problem handler
 * implementation via map container, defined in {@link ProblemConfig} by key as inner enum {@link com.harlyn.domain.problems.Problem.ProblemType}
 * Handlers process solution handling. If handlers not manual, checkSolution must return valid boolean value for this solution,
 * otherwise result of checkSolution will not be taken into account, so you can return any value.
 *
 * If you want to add new handler you shoul do thi steps:
 * <ul>
 *     <li>
 *         Write new implementation of {@link ProblemHandler}.
 *         This implementation must be tread-safe,better if this be just stateless
 *     </li>
 *     <li>
 *         Create enum value in {@link com.harlyn.domain.problems.Problem.ProblemType},
 *         corresponding to this implementation
 *     </li>
 *     <li>
 *         In {@link ProblemConfig} define bean from new implementation and put it by key from created enum instance
 *     </li>
 * </ul>
 */
public interface ProblemHandler {
    /**
     * @return Is problem handler not responsible for generating final decision about solution status.
     */
    boolean isManual();

    /**
     * Process solution and return it correctness.
     * If handler is manual, handler after processing solution my return any answer.
     *
     * @param problem
     * @param solution
     * @param solver
     * @return Is solution right?
     */
    boolean checkSolution(Problem problem, SubmitData solution, User solver);
}
